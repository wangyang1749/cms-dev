package com.wangyang.web.controller.api;

import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.enums.TemplateType;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.params.TemplateParam;
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


    /**
     * 根据template类型获取Template
     * @param type
     * @return
     */
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

//    @GetMapping("/setStatus/{id}")
//    public Template setStatus(@PathVariable("id") int id){
//        Template template = templateService.setStatus(id);
//        htmlService.generateHome();
//        return template;
//    }

    @PostMapping
    public Template add(@RequestBody Template template){
        return templateService.add(template);
    }

    public void deleteById(Integer id){
        templateService.deleteById(id);
    }
}
