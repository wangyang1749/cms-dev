package com.wangyang.pojo.support;

import lombok.Data;
import org.springframework.http.MediaType;

@Data
public class UploadResult {
    private String filename;

    private String filePath;

    private String key;

    private String thumbPath;

    private String suffix;

    private MediaType mediaType;

    private Long size;

    private Integer width;

    private Integer height;


}
