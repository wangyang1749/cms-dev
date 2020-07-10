package com.wangyang.data.service;

import com.wangyang.model.pojo.dto.CategoryDto;
import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.params.CategoryQuery;
import com.wangyang.model.pojo.support.TemplateOptionMethod;
import com.wangyang.model.pojo.vo.CategoryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ICategoryService{
    Category save(Category category);

    /**
     * add category
     * @param category
     * @return
     */
    Category addOrUpdate(Category category);


    Page<CategoryDto> pageBy(String categoryEnName, int page, int size);

    Page<CategoryDto> pageBy(String categoryEnName, Pageable pageable);

    List<CategoryDto> listBy(String categoryEnName);



    List<Category> findAllById(Iterable<Integer> ids);

    /**
     * delete by Id
     * @param id
     */
    Category deleteById(int id);



    /**
     * find category by id
     * @param id
     * @return
     */
    Category findById(int id);




    List<CategoryDto> listRecommend();

    Optional<Category> findOptionalById(int id);

    List<Category> list(CategoryQuery categoryQuery, Sort sort);

    List<CategoryDto> listAllDto();

    List<Category> listAll();

    /**
     * 生成菜单树
     * @return
     */
    List<CategoryVO> listCategoryVo();


    List<Category> list();

    Category recommendOrCancelHome(int id);

    Category haveHtml(int id);

    Category addOrRemoveToMenu(int id);

    Category findByViewName(String viewName);
}
