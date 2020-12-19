package com.wangyang.service.service;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Menu;

import java.util.List;

public interface IMenuService {
    Menu add(Menu menu);

    Menu findById(int id);

    Menu update(int id, Menu updateMenu);

    void delete(int id);

    Menu removeCategoryToMenu(int id);

    Menu addCategoryToMenu(Category category);

    List<Menu> list();
}
