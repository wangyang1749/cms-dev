package com.wangyang.data.handle;

import com.wangyang.model.pojo.enums.AttachmentType;
import com.wangyang.model.pojo.support.UploadResult;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {
    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    boolean supportType(@Nullable AttachmentType type);

    @NonNull
    UploadResult upload(@NonNull MultipartFile file);



    @NonNull
    UploadResult upload(@NonNull MultipartFile file,String name);

    UploadResult upload(String url, String name);

    /**
     * 上传字符串文件
     * @param content
     * @param strName
     * @return
     */
    UploadResult uploadStrContent(String content,String strName);

    void delete(@NonNull String key);

    static boolean isImageType(@Nullable MediaType mediaType) {
        return mediaType != null && IMAGE_TYPE.includes(mediaType);
    }

}
