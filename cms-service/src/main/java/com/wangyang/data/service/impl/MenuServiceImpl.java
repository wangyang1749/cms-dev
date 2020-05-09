package com.wangyang.data.service.impl;


import com.wangyang.common.exception.ObjectException;
import com.wangyang.data.service.IComponentsService;
import com.wangyang.data.service.IMenuService;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.data.repository.MenuRepository;
import com.wangyang.model.pojo.entity.Menu;
import com.wangyang.model.pojo.support.TemplateOption;
import com.wangyang.model.pojo.support.TemplateOptionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//@TemplateOption
public class MenuServiceImpl implements IMenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    IComponentsService templatePageService;


    @Override
    public Menu add(Menu menu){
        Menu saveMenu = menuRepository.save(menu);
        return saveMenu;
    }

    @Override
    public Menu findById(int id){
        Optional<Menu> menuOptional = menuRepository.findById(id);
        if(!menuOptional.isPresent()){
            throw new ObjectException("Update menu did't exist!!");
        }
        Menu menu =menuOptional.get();
        return menu;
    }
    @Override
    public Menu update(int id, Menu updateMenu){
        Menu menu = findById(id);
        BeanUtils.copyProperties(updateMenu,menu,"id");
        Menu saveMenu = menuRepository.save(menu);
        return  saveMenu;
    }


    @Override
    public void delete(int id){
        menuRepository.deleteById(id);
    }






    @Override
//    @TemplateOptionMethod(name = "Menu",templateValue = "templates/components/@header",viewName="header",path = "components")
    public List<Menu> list(){
        return menuRepository.findAll();
    }


}
