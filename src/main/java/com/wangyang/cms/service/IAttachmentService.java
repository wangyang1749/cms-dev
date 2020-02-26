package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Attachment;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface IAttachmentService {
    Attachment add(Attachment attachment);
    Attachment upload(@NonNull MultipartFile file);
}
