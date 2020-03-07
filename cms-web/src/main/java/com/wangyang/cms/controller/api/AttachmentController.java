package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Attachment;
import com.wangyang.cms.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    IAttachmentService attachmentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Attachment upload(@RequestPart("file") MultipartFile file){
        return  attachmentService.upload(file);
    }

    @GetMapping
    public Page<Attachment> list(@PageableDefault (sort = {"id"},direction = DESC)Pageable pageable){
        return attachmentService.list(pageable);
    }

    @RequestMapping("/delete/{id}")
    public  Attachment deleteById(@PathVariable("id") Integer id){
        return attachmentService.deleteById(id);
    }
    @RequestMapping("/delete")
    public List<Attachment> deleteByIds(Collection ids){
        return attachmentService.deleteByIds(ids);
    }



}
