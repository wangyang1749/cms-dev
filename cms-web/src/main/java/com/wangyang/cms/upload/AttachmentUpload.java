package com.wangyang.cms.upload;

import com.wangyang.data.handle.FileHandlers;
import com.wangyang.data.repository.AttachmentRepository;
import com.wangyang.data.service.IOptionService;
import com.wangyang.model.pojo.entity.Attachment;
import com.wangyang.model.pojo.enums.AttachmentType;
import com.wangyang.model.pojo.enums.PropertyEnum;
import com.wangyang.model.pojo.support.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class AttachmentUpload {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    FileHandlers fileHandlers;

    @Autowired
    IOptionService optionService;


    public Attachment upload(MultipartFile file) {
        //TODO AttachmentType.LOCAL from databases
        UploadResult uploadResult = fileHandlers.upload(file, getAttachmentType());
        Attachment attachment = new Attachment();
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f-thumbnail.png
        attachment.setPath(uploadResult.getFilePath());
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
        attachment.setFileKey(uploadResult.getKey());
        ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f-thumbnail.png
        attachment.setThumbPath(uploadResult.getThumbPath());
        //image/png
        attachment.setMediaType(uploadResult.getMediaType().toString());
        //png
        attachment.setSuffix(uploadResult.getSuffix());
        attachment.setWidth(uploadResult.getWidth());
        attachment.setHeight(uploadResult.getHeight());
        attachment.setSize(uploadResult.getSize());
        attachment.setType( AttachmentType.LOCAL);
        return attachmentRepository.save(attachment);
    }
    public AttachmentType getAttachmentType(){
        String propertyValue = optionService.getPropertyStringValue(PropertyEnum.ATTACHMENT_TYPE);
        if(propertyValue==null){
            return AttachmentType.LOCAL;
        }
        return AttachmentType.valueOf(propertyValue);
    }


}
