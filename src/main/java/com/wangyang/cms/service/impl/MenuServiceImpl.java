package com.wangyang.cms.service.impl;

import com.wangyang.cms.core.jms.producer.IProducerService;
import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.entity.Menu;
import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.repository.MenuRepository;
import com.wangyang.cms.service.IMenuService;
import com.wangyang.cms.service.IComponentsService;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@TemplateOption
public class MenuServiceImpl implements IMenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    IComponentsService templatePageService;

    @Autowired
    IProducerService producerService;

    @Override
    public Menu add(Menu menu){
        Menu saveMenu = menuRepository.save(menu);
        covertHtml();
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
        covertHtml();
        return  saveMenu;
    }


    @Override
    public void delete(int id){
        menuRepository.deleteById(id);
        covertHtml();
    }


    private void covertHtml() {
        List<Menu> list = list();
        Components templatePage = templatePageService.findByDataName("menuServiceImpl.list");
        TemplateUtil.convertHtmlAndSave(list,templatePage);
    }



    @Override
    @TemplateOptionMethod(name = "Menu",templateValue = "templates/components/@header",viewName="header",path = "components")
    public List<Menu> list(){
        return menuRepository.findAll();
    }


}
