package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.expection.TemplateException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.params.CategoryQuery;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.repository.*;
import com.wangyang.cms.service.*;
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
//    @Autowired
//    ArticleCategoryRepository articleCategoryRepository;


    @Autowired
    IOptionService optionService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    IComponentsService templatePageService;





    @Override
    public Category save(Category category){
        return categoryRepository.save(category);
    }
    @Override
    public Category addOrUpdate(Category category) {
//        Category category = new Category();
//        BeanUtils.copyProperties(categoryParam, category);
//        if(category.getParentId()==null){
////            throw  new ObjectException(category.getName()+"的父Id不能为空！！");
//            category.setPath("articleList");
//            category.setSelfListViewName("DEFAULT_ARTICLE_LIST");
//            category.setParentId(-1);
//        }

        if (StringUtils.isEmpty(category.getViewName())) {
            String viewName = CMSUtils.randomViewName();
            category.setViewName(viewName);
        }

//        if (category.getParentId()==0){
//            if(category.getPath()==null){
//                throw new ObjectException(category.getName()+"的路径不能为空！");
//            }
//            return categoryRepository.save(category);
//        }
        if(category.getHaveHtml()==null){
            category.setHaveHtml(true);
        }

        if(category.getTemplateName()==null||"".equals(category.getTemplateName())){
            category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
        }
        if(category.getArticleTemplateName()==null||"".equals(category.getArticleTemplateName())){
            category.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }
        //从父类获取该列表组的生成路径和视图名称
//        if(category.getParentId()!=0&&category.getParentId()!=-1){
//            Optional<Category> optionalCategory = findOptionalById(category.getParentId());
//            if(!optionalCategory.isPresent()){
//                throw  new ObjectException(category.getName()+"的父分类不存在！");
//            }
//            category.setPath(optionalCategory.get().getPath());
//            category.setSelfListViewName(optionalCategory.get().getViewName());
//        }

        category.setPath(CmsConst.CATEGORY_LIST_PATH);
//        category.setSelfListViewName(category.getTemplateName());
        //摘除文章列表，用于其它页面组装
//        category.setArticleListViewName("__"+category.getViewName());
        //页面本身的viewName，这个不应该依赖于category.getViewName()文章列表

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
    public ModelAndView preview(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Category category = findById(id);
        //预览
        CategoryArticleListDao articleListVo = articleService.findCategoryArticleBy(category,0);

        Template template = templateService.findByEnName(category.getTemplateName());
        modelAndView.addObject("view", articleListVo);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }



    /**
     * generate components
     */
    @Override
    public void generateListHtml() {
        Components templatePage = templatePageService.findByDataName("categoryServiceImpl.listAsTree");
        TemplateUtil.convertHtmlAndSave(list(), templatePage);
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
    @TemplateOptionMethod(name = "Category List", templateValue = "templates/components/@categoryList", viewName = "categoryList", path = "components")
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

//    private List<CategoryVO> createTree(int pid, List<CategoryVO> categories) {
//        List<CategoryVO> treeCategory = new ArrayList<>();
//        for (CategoryVO categoryVO : categories) {
//            if (pid == categoryVO.getParentId()) {
//                treeCategory.add(categoryVO);
//            }
//        }
//        return treeCategory;
//    }
}