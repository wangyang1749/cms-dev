package com.wangyang.cms.service.impl;

import com.wangyang.cms.handle.FileHandlers;
import com.wangyang.cms.pojo.entity.Attachment;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.UploadResult;
import com.wangyang.cms.repository.AttachmentRepository;
import com.wangyang.cms.service.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    FileHandlers fileHandlers;

    @Override
    public Attachment add(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment upload(MultipartFile file) {
        //TODO AttachmentType.LOCAL from databases
        UploadResult uploadResult = fileHandlers.upload(file, AttachmentType.LOCAL);
        Attachment attachment = new Attachment();

        attachment.setPath(uploadResult.getFilePath());
        attachment.setFileKey(uploadResult.getKey());
        attachment.setMediaType(uploadResult.getMediaType().toString());
        attachment.setSuffix(uploadResult.getSuffix());

        attachment.setSize(uploadResult.getSize());
//        attachment.setType(attachmentType);
        return attachmentRepository.save(attachment);
    }
}
