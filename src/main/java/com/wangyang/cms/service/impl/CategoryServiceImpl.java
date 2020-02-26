package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.expection.TemplateException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.support.TemplateConst;
import com.wangyang.cms.pojo.vo.CategoryDetailVO;
import com.wangyang.cms.pojo.vo.CategoryVO;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.repository.SheetRepository;
import com.wangyang.cms.repository.TemplateRepository;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.utils.CMSUtils;
import com.wangyang.cms.utils.TemplateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    SheetRepository sheetRepository;
    @Autowired
    ArticleRepository articleRepository;


    @Override
    public Category add(Category category) {
        if(category.getViewName()==null||"".equals(category.getViewName())){
            category.setViewName(CMSUtils.randomViewName());
        }
        if(category.getHaveHtml()){
            covertHtml(category);
        }
        //generate category page
        return categoryRepository.save(category);
    }

    private String covertHtml(Category category) {
        Optional<Template> template = templateRepository.findById(category.getTemplateId());
        if(!template.isPresent()){
            throw new TemplateException("Category Template not found !!");
        }
        return  TemplateUtil.convertHtmlAndSave(category,template.get());
    }


    @Override
    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category update(int id, CategoryParam categoryParam) {
        Category category = findById(id);
        BeanUtils.copyProperties(categoryParam,category);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category findById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            return optionalCategory.get();
        }

        throw new ObjectException("Category not found");
    }

    @Override
    public List<ArticleDto> findArticleById(int id) {
        List<Article> articles = articleRepository.findByCategoryId(id);
        List<ArticleDto> articleDtos = articles.stream().map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article, articleDto);
            return articleDto;
        }).collect(Collectors.toList());
        return articleDtos;
    }

    @Override
    public CategoryDetailVO findCategoryDetailVOByID(int id) {
        Category category = findById(id);
        CategoryDetailVO categoryDetailVO = new CategoryDetailVO();
        BeanUtils.copyProperties(category,categoryDetailVO);
        List<ArticleDto> articleDtos = findArticleById(id);
        categoryDetailVO.setArticleVOList(articleDtos);
        return categoryDetailVO;
    }

    @Override
    public Page<CategoryDto> list(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category,categoryDto);
            return categoryDto;
        });
    }

    @Override
    public List<CategoryDto> listAll() {

        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());

    }

    @Override
    public List<CategoryVO> listAsTree(Sort sort) {
        List<Category> categories = categoryRepository.findAll(sort);
        // generate tree
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        List<CategoryVO> categoryVOS = categories.stream().map(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);
            return categoryVO;
        }).collect(Collectors.toList());
        return createTree(0,categoryVOS) ;
    }

    @Override
    public ModelAndView preview(Integer id){
        ModelAndView modelAndView = new ModelAndView();
        CategoryDetailVO categoryDetailVO = findCategoryDetailVOByID(id);
        Optional<Template> template = templateRepository.findById(categoryDetailVO.getTemplateId());
        if(!template.isPresent()){
            throw new TemplateException("Category Template not found !!");
        }
        modelAndView.setViewName(template.get().getTemplateValue());
        return modelAndView;
    }

    private List<CategoryVO> createTree(int pid,List<CategoryVO> categories){
        List<CategoryVO> treeCategory = new ArrayList<>();
        for(CategoryVO categoryVO: categories){
            if(pid == categoryVO.getParentId()){
                treeCategory.add(categoryVO);
                categoryVO.setChildren(createTree(categoryVO.getId(),categories));
            }
        }
        return treeCategory;
    }


}
