package com.wangyang.cms.controller.api;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.data.service.IAttachmentService;
import com.wangyang.model.pojo.entity.Attachment;
import com.wangyang.model.pojo.params.AttachmentParam;
import org.jpmml.model.annotations.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    /**
     * 上传文字内容到文件 SVG
     * @param attachmentParam
     * @return
     */
    @RequestMapping(value = "/uploadStrContent")
    public Attachment uploadStrContent(@RequestBody AttachmentParam attachmentParam){
        return  attachmentService.uploadStrContent(attachmentParam);
    }

    @RequestMapping(value = "/find/{id}")
    public Attachment findById(@PathVariable("id") Integer id){
       return  attachmentService.findById(id);
    }

    /**
     * 更新文字内容
     * @param attachmentId
     * @param attachmentParam
     * @return
     */
    @RequestMapping(value = "/uploadStrContent/{attachmentId}")
    public Attachment updateStrContent(@PathVariable("attachmentId")Integer attachmentId,@RequestBody  AttachmentParam attachmentParam){
        return  attachmentService.uploadStrContent(attachmentId,attachmentParam);
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
