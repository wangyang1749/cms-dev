package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Menu;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.params.CategoryQuery;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.CategoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

public interface ICategoryService extends IBaseCategoryService<Category> {
    Category save(Category category);

    /**
     * add category
     * @param category
     * @return
     */
    Category addOrUpdate(Category category);

//    CategoryArticleListDao getArticleListByCategory(Category category);

    /**
     * 动态分页
     * @param categoryId
     * @param page
     * @return
     */
//    ModelAndView getArticleListByCategory(int categoryId,int page);

//    List<Integer> updateAllCategoryHtml();

    List<Category> findAllById(Iterable<Integer> ids);

    /**
     * delete by Id
     * @param id
     */
    Category deleteById(int id);


//    Category update(Category category);

    void generateListHtml();

    /**
     * find category by id
     * @param id
     * @return
     */
    Category findById(int id);
//    CategoryArticleListDao findCategoryDetailVOByID(int id);
    /**
     * category page
     * @return
     */
//    Page<CategoryVO> list(Pageable pageable);

    List<CategoryDto> listAll();

//    List<CategoryVO> listAsTree(@NonNull Sort sort);
//    List<CategoryVO> listAsTree();
    ModelAndView preview(Integer id);

    List<CategoryDto> listRecommend();

    Optional<Category> findOptionalById(int id);

    List<Category> list(CategoryQuery categoryQuery, Sort sort);

    @TemplateOptionMethod(name = "Category List",templateValue = "templates/components/@categoryList",viewName="categoryList",path = "components")
    List<Category> list();

    Category recommendOrCancelHome(int id);

    Category haveHtml(int id);

    Category addOrRemoveToMenu(int id);
}
