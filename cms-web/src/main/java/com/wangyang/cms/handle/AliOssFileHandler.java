package com.wangyang.cms.handle;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.wangyang.cms.expection.FileOperationException;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.support.UploadResult;
import com.wangyang.cms.service.IOptionService;
import com.wangyang.cms.utils.FilenameUtils;
import com.wangyang.cms.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class AliOssFileHandler implements FileHandler {

    @Autowired
    IOptionService optionService;

    @Override
    public boolean supportType(AttachmentType type) {
        return AttachmentType.ALIOSS.equals(type);
    }

    @Override
    public UploadResult upload(MultipartFile file) {

        // Get config
        String endPoint = optionService.getPropertyValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyValue(PropertyEnum.BUCKET_NAME);
        String domain = optionService.getPropertyValue(PropertyEnum.OSS_DOMAIN);
        String source = optionService.getPropertyValue(PropertyEnum.OSS_SOURCE);
        String styleRule = optionService.getPropertyValue(PropertyEnum.OSS_STYLE_RULE);
        String thumbnailStyleRule = optionService.getPropertyValue(PropertyEnum.OSS_THUMBNAIL_STYLE_RULE);

        StringBuilder basePath = new StringBuilder(domain);
        basePath.append(bucketName)
                .append(".")
                .append(endPoint)
                .append("/");

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
        try {
            String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());
            StringBuilder upFilePath = new StringBuilder();
            if (StringUtils.isNotEmpty(source)) {
                upFilePath.append(source)
                        .append("/");
            }
            upFilePath.append(basename)
                    .append("_")
                    .append(timestamp)
                    .append(".")
                    .append(extension);

            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());
            log.info(basePath.toString());

            PutObjectResult putObjectResult = ossClient.putObject(bucketName, upFilePath.toString(), file.getInputStream());
            if(putObjectResult==null){
                throw  new FileOperationException("File upload failed!!");
            }
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
            uploadResult.setKey(upFilePath.toString());
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());
            uploadResult.setSize(file.getSize());

            // Handle thumbnail
            if (FileHandler.isImageType(uploadResult.getMediaType())) {
                BufferedImage image = ImageUtils.getImageFromFile(file.getInputStream(), extension);
                uploadResult.setWidth(image.getWidth());
                uploadResult.setHeight(image.getHeight());
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    uploadResult.setThumbPath(filePath);
                } else {
                    uploadResult.setThumbPath(StringUtils.isBlank(thumbnailStyleRule) ? filePath : filePath + thumbnailStyleRule);
                }
            }

            log.info("Uploaded file: [{}] successfully", file.getOriginalFilename());
            return uploadResult;
        } catch (IOException e) {
            throw  new FileOperationException("File upload failed!!");
        }finally {
            ossClient.shutdown();
        }

    }

    @Override
    public void delete(String key) {
        Assert.notNull(key, "File key must not be blank");

        // Get config
        String endPoint = optionService.getPropertyValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyValue(PropertyEnum.BUCKET_NAME);

        // Init OSS client
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);

        try {
            ossClient.deleteObject(new DeleteObjectsRequest(bucketName).withKey(key));
        } catch (Exception e) {
            throw new FileOperationException("附件 " + key + " 从阿里云删除失败!!!");
        } finally {
            ossClient.shutdown();
        }
    }
}
