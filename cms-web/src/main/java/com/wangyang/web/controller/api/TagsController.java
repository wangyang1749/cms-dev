package com.wangyang.web.controller.api;

import com.wangyang.service.service.ITagsService;
import com.wangyang.pojo.dto.TagsDto;
import com.wangyang.pojo.entity.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

    @Autowired
    ITagsService tagsService;

    @PostMapping
    public Tags add(@RequestBody  Tags tags){
        return tagsService.add(tags);
    }
    @GetMapping
    public List<TagsDto> list(){
        return tagsService.listAll();
    }
}
