package com.wangyang.cms.controller.api;

import com.wangyang.data.service.ITagsService;
import com.wangyang.model.pojo.dto.TagsDto;
import com.wangyang.model.pojo.entity.Tags;
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
