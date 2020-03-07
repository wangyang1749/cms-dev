package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Menu;

import java.util.List;

public interface IMenuService {
    Menu add(Menu menu);

    Menu findById(int id);

    Menu update(int id, Menu updateMenu);

    void delete(int id);

    List<Menu> list();
}
