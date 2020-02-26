package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Attachment;
import com.wangyang.cms.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    IAttachmentService attachmentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Attachment upload(@RequestPart("file") MultipartFile file){
        return  attachmentService.upload(file);
    }
}
