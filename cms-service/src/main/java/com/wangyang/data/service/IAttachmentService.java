package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface IAttachmentService {
    Attachment add(Attachment attachment);
    Attachment upload(@NonNull MultipartFile file);

    Page<Attachment> list(Pageable pageable);

    Attachment deleteById(int id);

    List<Attachment> deleteByIds(Collection<Integer> ids);

    Attachment findById(int id);
}
