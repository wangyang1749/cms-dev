package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.vo.CategoryVO;
import com.wangyang.cms.repository.*;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.IComponentsService;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.utils.CMSUtils;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@TemplateOption
@Transactional
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ITemplateService templateService;
    @Autowired
    IArticleService articleService;
    @Autowired
    ArticleCategoryRepository articleCategoryRepository;

    @Autowired
    IComponentsService templatePageService;


    @Override

    public Category add(CategoryParam categoryParam) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryParam,category);
        if(StringUtils.isEmpty(category.getViewName())){
           String viewName = CMSUtils.randomViewName();
           category.setViewName(viewName);
        }
        if(category.getParentId()!=0){
            Category parentCategory = findById(category.getTemplateId());
            parentCategory.setHaveChildren(true);
            categoryRepository.save(parentCategory);
        }
        Category saveCategory = categoryRepository.save(category);
        //TODO
        generateListHtml();
        if(saveCategory.getHaveHtml()){
            convertHtml(category);
        }
        return saveCategory;
    }

    @Override
    public Category update(int id, CategoryParam categoryParam) {
        Category category = findById(id);
        TemplateUtil.deleteTemplateHtml(category.getViewName(),category.getPath());
        BeanUtils.copyProperties(categoryParam,category);
        Category updateCategory = categoryRepository.save(category);
        //TODO
        generateListHtml();
        if(updateCategory.getHaveHtml()){
            convertHtml(updateCategory);
        }
        return updateCategory;
    }


    @Override
    public ModelAndView preview(Integer id){
        ModelAndView modelAndView = new ModelAndView();
        Category category = findById(id);
        CategoryArticleListDao articleListVo = getArticleListByCategory(category);

        Template template = templateService.findById(category.getTemplateId());
        modelAndView.addObject("view",articleListVo);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }

    @Override
    public ModelAndView getArticleListByCategory(int categoryId, int page){
        ModelAndView modelAndView =new ModelAndView();
        Category category = findById(categoryId);

        CategoryArticleListDao articleListByCategory = getArticleListByCategory(category, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")));

        Template template = templateService.findById(category.getTemplateId());

        modelAndView.setViewName(template.getTemplateValue());
        modelAndView.addObject("view",articleListByCategory);
        return modelAndView;
    }


    @Override
    public CategoryArticleListDao getArticleListByCategory(Category category){
        CategoryArticleListDao categoryArticleListDao = getArticleListByCategory(category, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
        if(category.getHaveChildren()){
            List<Category> categoryList = categoryRepository.findAll(new Specification<Category>() {
                @Override
                public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    return criteriaBuilder.equal(root.get("parentId"), category.getId());
                }
            });
            categoryArticleListDao.setChildren(categoryList);
            log.debug("##这个category有子节点!!");
        }
        if(category.getParentId()!=0){
            List<Category> categoryList = categoryRepository.findAll(new Specification<Category>() {
                @Override
                public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    return criteriaBuilder.equal(root.get("id"), category.getParentId());
                }
            });

            categoryArticleListDao.setParent(categoryList.get(0));
            log.debug("##这个category有父节点!!");
        }

        return categoryArticleListDao;
    }



    private CategoryArticleListDao getArticleListByCategory(Category category,Pageable pageable){
        CategoryArticleListDao articleListVo = new CategoryArticleListDao();
        Page<ArticleDto> articleDtoPage = articleService.findArticleListByCategoryId(category.getId(), pageable);
        articleListVo.setPage(articleDtoPage);
        articleListVo.setCategory(category);
        articleListVo.setViewName(category.getViewName());
        return articleListVo;
    }

    /**
     * generate article list title info`
     * @param category
     */
    private void convertHtml(Category category) {
        CategoryArticleListDao articleListVo = getArticleListByCategory(category);

        Template template = templateService.findById(category.getTemplateId());
        TemplateUtil.convertHtmlAndSave(articleListVo,template);
    }

    /**
     * generate components
     */
    private void generateListHtml() {
        Components templatePage = templatePageService.findByDataName("categoryServiceImpl.listAsTree");
        TemplateUtil.convertHtmlAndSave(listAsTree(),templatePage);
    }

    @Override
    public void deleteById(int id) {
        Category category = findById(id);
        TemplateUtil.deleteTemplateHtml(category.getViewName(),category.getPath());
        log.info("### delete category"+ category.getName());
        categoryRepository.deleteById(id);

        //TODO
        List<ArticleCategory> articleCategories = articleCategoryRepository.deleteByCategoryId(id);
        log.info("### delete  article category"+articleCategories.size());
        generateListHtml();
    }



    @Override
    public Category findById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            return optionalCategory.get();
        }

        throw new ObjectException("Category not found");
    }



//    @Override
//    public CategoryArticleListDao findCategoryDetailVOByID(int id) {
//        Category category = findById(id);
//        return  getArticleListByCategory(category);
//
//    }



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

        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
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
    @TemplateOptionMethod(name = "Category List",templateValue = "templates/components/@categoryList",viewName="categoryList",path = "components")
    public List<CategoryVO> listAsTree() {
        return listAsTree(Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
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

    @Override
    public   List<Category> findCategoryByArticleId(int articleId) {
        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Subquery<ArticleCategory> subquery = criteriaQuery.subquery(ArticleCategory.class);
                Root<ArticleCategory> subRoot = subquery.from(ArticleCategory.class);
                subquery  = subquery.select(subRoot.get("categoryId"))
                        .where(criteriaBuilder.equal(subRoot.get("articleId"),articleId));
                return root.get("id").in(subquery);
            }
        };
        List<Category> categories = categoryRepository.findAll(specification);
        return categories;
    }
}
