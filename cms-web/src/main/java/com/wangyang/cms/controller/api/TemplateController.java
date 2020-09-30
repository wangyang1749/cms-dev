package com.wangyang.cms.controller.api;

import com.wangyang.data.service.IHtmlService;
import com.wangyang.data.service.ITemplateService;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.enums.TemplateType;
import com.wangyang.model.pojo.entity.Template;
import com.wangyang.model.pojo.params.ComponentsParam;
import com.wangyang.model.pojo.params.TemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/template")
public class TemplateController {
    @Autowired
    ITemplateService templateService;

    @Autowired
    IHtmlService htmlService;


    public List<String> listTemplateType(){
        return null;
    }

    @GetMapping("/find/{type}")
    public List<Template> findByType(@PathVariable("type") TemplateType type){

        return templateService.findByTemplateType(type);
    }

    @PostMapping("/update/{id}")
    public Template update(@PathVariable("id") Integer id, @RequestBody TemplateParam templateParam){
        return templateService.update(id,templateParam);
    }

    @GetMapping("/findDetailsById/{id}")
    public Template findDetailsById(@PathVariable("id") Integer id){
        return templateService.findDetailsById(id);
    }


    @GetMapping
    public Page<Template> list(@PageableDefault(sort = {"id"},direction = DESC)Pageable pageable){
        Page<Template> templatePage = templateService.list(pageable);

        return templatePage;
    }

    @GetMapping("/setStatus/{id}")
    public Template setStatus(@PathVariable("id") int id){
        Template template = templateService.setStatus(id);
        htmlService.generateHome();
        return template;
    }

    @PostMapping
    public Template add(@RequestBody Template template){
        return templateService.add(template);
    }

    public void deleteById(Integer id){
        templateService.deleteById(id);
    }
}
