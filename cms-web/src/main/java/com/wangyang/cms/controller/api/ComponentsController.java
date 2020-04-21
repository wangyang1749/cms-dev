package com.wangyang.cms.controller.api;

import com.wangyang.cms.util.TemplateUtil;
import com.wangyang.data.service.IComponentsService;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.params.ComponentsParam;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Components add(Components templatePage){
        return componentsService.add(templatePage);
    }

    @PostMapping("/update/{id}")
    public Components update(@PathVariable("id") Integer id, ComponentsParam templatePageParam){
        return componentsService.update(id,templatePageParam);
    }

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        componentsService.delete(id);
    }


    @GetMapping("/generate/{id}")
    public BaseResponse generateHtml(@PathVariable("id") Integer id){
        Components templatePage = componentsService.findById(id);
        Object o = componentsService.getModel(templatePage.getDataName());
        return BaseResponse.ok(TemplateUtil.convertHtmlAndSave(o, templatePage));
    }


}
