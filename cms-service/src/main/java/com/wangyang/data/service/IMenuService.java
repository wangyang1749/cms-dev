package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.Menu;

import java.util.List;

public interface IMenuService {
    Menu add(Menu menu);

    Menu findById(int id);

    Menu update(int id, Menu updateMenu);

    void delete(int id);

    List<Menu> list();
}
