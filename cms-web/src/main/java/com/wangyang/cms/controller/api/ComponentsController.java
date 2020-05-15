package com.wangyang.cms.controller.api;

import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.service.IComponentsService;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.params.ComponentsParam;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/templatePage")
public class ComponentsController {

    @Autowired
    IComponentsService componentsService;

    @GetMapping
    public Page<Components> list(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return  componentsService.list(pageable);
    }

    @RequestMapping("/find/{id}")
    public Components findById(@PathVariable("id") Integer id){
        return componentsService.findById(id);
    }

    @PostMapping
    public Components add(@RequestBody  ComponentsParam componentsParam){
        return componentsService.add(componentsParam);
    }



    @PostMapping("/update/{id}")
    public Components update(@PathVariable("id") Integer id,@RequestBody  ComponentsParam templatePageParam){
        return componentsService.update(id,templatePageParam);
    }





    @RequestMapping("/delete/{id}")
    public Components delete(@PathVariable("id") Integer id){
        Components components = componentsService.delete(id);
        TemplateUtil.deleteTemplateHtml(components.getViewName(),components.getPath());
        return components;
    }


    @GetMapping("/generate/{id}")
    public BaseResponse generateHtml(@PathVariable("id") Integer id){
        Components components = componentsService.findById(id);
        Object o = componentsService.getModel(components);
        return BaseResponse.ok(TemplateUtil.convertHtmlAndSave(o, components));


    }


    @GetMapping("/listNeedArticle")
    public List<Components> listNeedArticle(){
        return componentsService.listNeedArticle();
    }


    @GetMapping("/findDetailsById/{id}")
    public Components findDetailsById(@PathVariable("id") Integer id){
        return componentsService.findDetailsById(id);
    }
}
