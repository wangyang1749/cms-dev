package com.wangyang.web.controller.api;

import com.wangyang.service.service.IOptionService;
import com.wangyang.pojo.entity.Option;
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
