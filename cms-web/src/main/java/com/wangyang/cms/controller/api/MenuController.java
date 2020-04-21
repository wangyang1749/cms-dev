package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Menu;
import com.wangyang.cms.service.IMenuService;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    IMenuService menuService;

    @PostMapping
    public Menu add(@RequestBody  Menu menu){
        return menuService.add(menu);
    }

    @GetMapping
    public List<Menu> list(){
        return menuService.list();
    }

    @PostMapping("/update/{id}")
    public Menu update(@RequestBody  Menu menu,@PathVariable("id") Integer id){
        return  menuService.update(id,menu);
    }

    @RequestMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id){
        menuService.delete(id);
        return BaseResponse.ok("Delete id "+id+" menu success!!");
    }
    @GetMapping("/find/{id}")
    public Menu findById(@PathVariable("id") Integer id){
        return menuService.findById(id);
    }
}
