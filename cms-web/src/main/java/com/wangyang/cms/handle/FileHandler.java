package com.wangyang.cms.handle;

import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.UploadResult;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {
    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    boolean supportType(@Nullable AttachmentType type);

    @NonNull
    UploadResult upload(@NonNull MultipartFile file);

    UploadResult upload(String url, String name);

    void delete(@NonNull String key);

    static boolean isImageType(@Nullable MediaType mediaType) {
        return mediaType != null && IMAGE_TYPE.includes(mediaType);
    }

}
