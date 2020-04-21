package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/option")
public class OptionController {

    @Autowired
    IOptionService optionService;

    @PostMapping
    public List<Option> addOption(@RequestBody List<Option> options){
        return optionService.saveUpdateOptionList(options);
    }
    @GetMapping
    public List<Option> list(){
        return optionService.list();
    }


}
