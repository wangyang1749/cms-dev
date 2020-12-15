package com.wangyang.service.service;

import com.wangyang.pojo.entity.Menu;

import java.util.List;

public interface IMenuService {
    Menu add(Menu menu);

    Menu findById(int id);

    Menu update(int id, Menu updateMenu);

    void delete(int id);

    List<Menu> list();
}
