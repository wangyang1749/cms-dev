package com.wangyang.cms.handle;

import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.UploadResult;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {
    /**
     * Checks if the given type is supported.
     *
     * @param type attachment type
     * @return true if supported; false or else
     */
    boolean supportType(@Nullable AttachmentType type);
    /**
     * Uploads file.
     *
     * @param file multipart file must not be null
     * @return upload result
     */
    @NonNull
    UploadResult upload(@NonNull MultipartFile file);
}
