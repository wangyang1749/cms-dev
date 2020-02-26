package com.wangyang.cms.handle;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.wangyang.cms.cache.StringCacheStore;
import com.wangyang.cms.pojo.enums.AliOssProperties;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.UploadResult;
import com.wangyang.cms.service.IOptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Component
public class AliOssFileHandler implements FileHandler {

    @Autowired
    IOptionService optionService;

    @Override
    public boolean supportType(AttachmentType type) {
        return AttachmentType.ALIOSS.equals(type);
    }

    @Override
    public UploadResult upload(MultipartFile file) {

//        optionService.getValue();
        AliOssProperties.ACCESS_KEY.getValue();
        String endPoint = optionService.getValue(AliOssProperties.END_POINT.getValue());
        String accessKey = optionService.getValue(AliOssProperties.ACCESS_KEY.getValue());
        String accessSecret = optionService.getValue(AliOssProperties.ACCESS_SECRET.getValue());
        String bucketName = optionService.getValue(AliOssProperties.BUCKET_NAME.getValue());
        StringBuilder upFilePath = new StringBuilder();

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, upFilePath.toString(), file.getInputStream());

            // Response result
            UploadResult uploadResult = new UploadResult();
//            uploadResult.setFilename(basename);
//            uploadResult.setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
//            uploadResult.setKey(upFilePath.toString());
//            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
//            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            return uploadResult;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }

        return new UploadResult();
    }
}
