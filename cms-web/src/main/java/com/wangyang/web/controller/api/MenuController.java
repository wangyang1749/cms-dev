package com.wangyang.web.controller.api;

import com.wangyang.service.service.IComponentsService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.IMenuService;
import com.wangyang.pojo.entity.Menu;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @Autowired
    IComponentsService convertHtmlAndSave;

    @Autowired
    IHtmlService htmlService;


    @PostMapping
    public Menu add(@RequestBody  Menu menu){
        Menu saveMenu = menuService.add(menu);
       htmlService.generateMenuListHtml();
        return saveMenu;
    }

    @GetMapping
    public List<Menu> list(){
        return menuService.list();
    }

    @PostMapping("/update/{id}")
    public Menu update(@RequestBody  Menu menu,@PathVariable("id") Integer id){
        Menu updateMenu = menuService.update(id, menu);
        htmlService.generateMenuListHtml();
        return updateMenu;
    }

    @RequestMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id){
        menuService.delete(id);
        htmlService.generateMenuListHtml();
        return BaseResponse.ok("Delete id "+id+" menu success!!");
    }
    @GetMapping("/find/{id}")
    public Menu findById(@PathVariable("id") Integer id){
        return menuService.findById(id);
    }
}
