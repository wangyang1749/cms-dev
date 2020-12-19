package com.wangyang.service.service.impl;


import com.wangyang.common.exception.ObjectException;
import com.wangyang.pojo.entity.Category;
import com.wangyang.service.service.IComponentsService;
import com.wangyang.service.service.IMenuService;
import com.wangyang.service.repository.MenuRepository;
import com.wangyang.pojo.entity.Menu;
import com.wangyang.service.util.FormatUtil;
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
    public Menu removeCategoryToMenu(int id){

        Menu menu = menuRepository.findByCategoryId(id);
        if(menu!=null){
            menuRepository.deleteById(menu.getId());
        }
        return menu;
    }

    @Override
    public Menu addCategoryToMenu(Category category){
        Menu menu = menuRepository.findByCategoryId(category.getId());
        if(menu==null){
            menu = new Menu();
        }

        menu.setName(category.getName());
        menu.setCategoryId(category.getId());
        menu.setUrlName(FormatUtil.categoryListFormat(category));
        menuRepository.save(menu);

        return  menu;
    }


    @Override
//    @TemplateOptionMethod(name = "Menu",templateValue = "templates/components/@header",viewName="header",path = "components")
    public List<Menu> list(){
        return menuRepository.findAll();
    }


}
