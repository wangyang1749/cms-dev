package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
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
import org.springframework.data.domain.PageRequest;
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
public class CategoryServiceImpl extends BaseCategoryServiceImpl<Category> implements ICategoryService {

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
        if(category.getHaveHtml()==null){
            category.setHaveHtml(true);
        }
        if (StringUtils.isEmpty(category.getViewName())) {
            String viewName = CMSUtils.randomViewName();
            category.setViewName(viewName);
        }
        if(category.getTemplateName()==null||"".equals(category.getTemplateName())){
            category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
        }
        Category saveCategory = categoryRepository.save(category);
        return saveCategory;
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
        categoryRepository.deleteById(id);
        return category;
    }

    @Override
    public ModelAndView preview(Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Category category = findById(id);
        //预览
        CategoryArticleListDao articleListVo = articleService.getArticleListByCategory(category);

//        Template template = templateService.findById(category.getTemplateId());
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
                   return  criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
               }
               return null;
            }
        };
        List<Category> categories = categoryRepository.findAll(specification,sort);
        return categories;
    }

    /**
     * 显示推荐的首页并且是生成html的
     * @return
     */
    @Override
    public List<CategoryDto> listRecommend(){
        CategoryQuery categoryQuery = new CategoryQuery();
        categoryQuery.setHaveHtml(true);
        categoryQuery.setRecommend(true);
        List<Category> categories = list(categoryQuery, Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
        return categories.stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
    }
    /**
     * 显示所有Category, 转化为Dto
     * @return
     */
    @Override
    public List<CategoryDto> listAll() {
        List<Category> categories = list(null, Sort.by(Sort.Order.desc("order")).and(Sort.by(Sort.Order.desc("id"))));
        return categories.stream().map(category -> {
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