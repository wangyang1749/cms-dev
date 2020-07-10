package com.wangyang.data.service.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.CMSUtils;
import com.wangyang.data.service.*;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.dto.CategoryArticleListDao;
import com.wangyang.model.pojo.dto.CategoryDto;
import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.entity.Menu;
import com.wangyang.model.pojo.entity.Template;
import com.wangyang.data.repository.CategoryRepository;
import com.wangyang.data.repository.MenuRepository;
import com.wangyang.model.pojo.params.CategoryQuery;
import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.support.TemplateOption;
import com.wangyang.model.pojo.support.TemplateOptionMethod;
import com.wangyang.model.pojo.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@TemplateOption
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
    IOptionService optionService;
    @Autowired
    MenuRepository menuRepository;


    @Override
    public Category save(Category category){
        return categoryRepository.save(category);
    }
    @Override
    public Category addOrUpdate(Category category) {

        if (StringUtils.isEmpty(category.getViewName())) {
            String viewName = CMSUtils.randomViewName();
            category.setViewName(viewName);
        }

        if(category.getHaveHtml()==null){
            category.setHaveHtml(true);
        }

        if(category.getTemplateName()==null||"".equals(category.getTemplateName())){
            category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
        }
        if(category.getArticleTemplateName()==null||"".equals(category.getArticleTemplateName())){
            category.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }


        category.setPath(CmsConst.CATEGORY_LIST_PATH);

        Category saveCategory = categoryRepository.save(category);
        return saveCategory;
    }

    @Override
    public Page<CategoryDto> pageBy(String categoryEnName, int page, int size){
        Sort sort = Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id")));
        return  pageBy(categoryEnName,PageRequest.of(page,size,sort));
    }

    @Override
    public Page<CategoryDto> pageBy(String categoryEnName,Pageable pageable){

        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("templateName"),categoryEnName);
            }
        };
        return  categoryRepository.findAll(specification,pageable).map(category -> {
                CategoryDto categoryDto = new CategoryDto();
                BeanUtils.copyProperties(category,categoryDto);
                return categoryDto;
        });
    }


    @Override
    public List<CategoryDto> listBy(String categoryEnName){
        Sort sort = Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id")));
        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("templateName"),categoryEnName);
            }
        };
        return  categoryRepository.findAll(specification,sort).stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category,categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());

    }

    /**
     * 显示推荐的首页并且是生成html的
     * @return
     */
    @Override
    public List<CategoryDto> listRecommend(){
        Sort sort = Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id")));
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setHaveHtml(true);
        categoryQuery.setRecommend(true);
        List<Category> categories = list(categoryQuery,sort );
        return categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
    }


    @Override
    public List<Category> findAllById(Iterable<Integer> ids){
        return categoryRepository.findAllById(ids);
    }

    /**
     * 重新生成所有Category的Html
     *
     * @return
     */
//    @Override
//    public List<Integer> updateAllCategoryHtml() {
//        List<Category> categories = categoryRepository.findAll();
//        categories.forEach(category -> {
//            cmsService.generateCategoryArticleListByCategory(category);
//        });
//        return ids;
//    }



    @Override
    public Category deleteById(int id) {
        Category category = findById(id);
        List<ArticleDto> articleDtos = articleService.listBy(category.getId());
        if(articleDtos.size()!=0){
            throw new ObjectException("不能删除该分类，由于存在"+articleDtos.size()+"篇文章！");
        }
        categoryRepository.deleteById(id);
        return category;
    }






    @Override
    public Category findById(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }

        throw new ObjectException("Category not found");
    }

    @Override
    public Optional<Category> findOptionalById(int id){
        return categoryRepository.findById(id);
    }



//    @Override
//    public CategoryArticleListDao findCategoryDetailVOByID(int id) {
//        Category category = findById(id);
//        return  getArticleListByCategory(category);
//
//    }

    //未使用
//    @Override
//    public Page<CategoryVO> list(Pageable pageable) {
//        Page<Category> categoryPage = categoryRepository.findAll(pageable);
//        return categoryPage.map(category -> {
//            CategoryVO categoryVO = new CategoryVO();
//            BeanUtils.copyProperties(category, categoryVO);
//            return categoryVO;
//        });
//    }





    @Override
    public List<Category> list(CategoryQuery categoryQuery,Sort sort) {
        Specification<Category> specification = new Specification<Category>() {
            @Override
            public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               if(categoryQuery!=null){
                   List<Predicate> predicates = new LinkedList<>();
                   if (categoryQuery.getRecommend() != null) {
                       predicates.add( criteriaBuilder.isTrue(root.get("recommend")));
                   }
                   if(categoryQuery.getHaveHtml()!=null){
                       predicates.add( criteriaBuilder.isTrue(root.get("haveHtml")));
                   }
                   if(categoryQuery.getParentId()!=null){
                       predicates.add(criteriaBuilder.equal(root.get("parentId"),categoryQuery.getParentId()));
                   }
                   return  criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
               }
               return null;
            }
        };
        List<Category> categories = categoryRepository.findAll(specification,sort);
        return categories;
    }


    /**
     * 显示所有Category, 转化为Dto
     * @return
     */
    @Override
    public List<CategoryDto> listAllDto() {
        return convertTo(listAll());
    }

    @Override
    public List<Category> listAll(){
        return categoryRepository.findAll(Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
    }

    @Override
    public List<CategoryVO> listCategoryVo() {
        List<Category> categories = listAll();
        return categories.stream().map(category -> {
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                return categoryVO;
        }).collect(Collectors.toList());
    }

    public List<CategoryDto> convertTo(List<Category> categories){
        return  categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
    }


    /**
     * 不显示没有生成Html的category
     * @return
     */
    @Override
//    @TemplateOptionMethod(name = "Category List", templateValue = "templates/components/@categoryList", viewName = "categoryList", path = "components")
    public List<Category> list() {
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setHaveHtml(true);
        return list(categoryQuery,Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
    }


    /**
     * 推荐分类到首页
     * @param id
     * @return
     */
    @Override
    public Category recommendOrCancelHome(int id){
        Category category = findById(id);
        if(category.getRecommend()){
            category.setRecommend(false);
        }else{
            category.setRecommend(true);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category haveHtml(int id){
        Category category = findById(id);
        if(category.getHaveHtml()){
            category.setHaveHtml(false);
        }else{
            category.setHaveHtml(true);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category addOrRemoveToMenu(int id){
        Category category = findById(id);
        Menu menu = menuRepository.findByCategoryId(category.getId());
        if(category.getExistNav()){
            category.setExistNav(false);
            if(menu!=null){
                menuRepository.deleteById(menu.getId());
            }
        }else{
            category.setExistNav(true);
            if(menu==null){
                menu = new Menu();
            }

            menu.setName(category.getName());
            menu.setCategoryId(category.getId());
            menu.setUrlName(category.getPath()+"/"+category.getViewName()+".html");
            menuRepository.save(menu);

        }
        return  categoryRepository.save(category);
    }

    @Override
    public Category findByViewName(String viewName){
        return categoryRepository.findByViewName(viewName);
    }
}