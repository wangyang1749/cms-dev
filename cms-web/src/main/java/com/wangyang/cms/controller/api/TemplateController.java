package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.service.ITemplateService;
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

    @GetMapping("/find/{type}")
    public List<Template> findByType(@PathVariable("type") TemplateType type){

        return templateService.findByTemplateType(type);
    }

    @GetMapping
    public Page<Template> list(@PageableDefault(sort = {"id"},direction = DESC)Pageable pageable){
        Page<Template> templatePage = templateService.list(pageable);

        return templatePage;
    }

    @PostMapping
    public Template add(@RequestBody Template template){
        return templateService.add(template);
    }

    public void deleteById(Integer id){
        templateService.deleteById(id);
    }
}
