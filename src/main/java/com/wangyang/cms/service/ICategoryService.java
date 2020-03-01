package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.vo.CategoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ICategoryService {
    /**
     * add category
     * @param categoryParam
     * @return
     */
    Category add(CategoryParam categoryParam);

    CategoryArticleListDao getArticleListByCategory(Category category);

    ModelAndView getArticleListByCategory(int categoryId,int page);

    /**
     * delete by Id
     * @param id
     */
    void deleteById(int id);

    /**
     * update category by id
     * @param id
     * @param categoryParam
     * @return
     */
    Category update(int id, CategoryParam categoryParam);

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
    Page<CategoryDto> list(Pageable pageable);

    List<CategoryDto> listAll();

    List<CategoryVO> listAsTree(@NonNull Sort sort);
    List<CategoryVO> listAsTree();
    ModelAndView preview(Integer id);

    List<Category> findCategoryByArticleId(int id);
}
