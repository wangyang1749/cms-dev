package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/option")
public class OptionController {

    @Autowired
    IOptionService optionService;
    @PostMapping("/add")
    public void addOption(@RequestBody List<Option> options){
        optionService.save(options);
    }
}
