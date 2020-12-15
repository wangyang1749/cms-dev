package com.wangyang.service.handle;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.wangyang.common.exception.FileOperationException;
import com.wangyang.service.service.IOptionService;
import com.wangyang.pojo.enums.AttachmentType;
import com.wangyang.pojo.enums.PropertyEnum;
import com.wangyang.common.utils.FilenameUtils;
import com.wangyang.common.utils.ImageUtils;
import com.wangyang.pojo.support.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        String endPoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String endPointPublic = optionService.getPropertyStringValue(PropertyEnum.END_POINT_PUBLIC);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyStringValue(PropertyEnum.BUCKET_NAME);
        String domain = optionService.getPropertyStringValue(PropertyEnum.OSS_DOMAIN);
        String source = optionService.getPropertyStringValue(PropertyEnum.OSS_SOURCE);
        String styleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_STYLE_RULE);
        String thumbnailStyleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_THUMBNAIL_STYLE_RULE);

        StringBuilder basePath = new StringBuilder(domain);
        basePath.append(bucketName)
                .append(".")
                .append(endPointPublic)
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
    public UploadResult upload(MultipartFile file, String name) {
        // Get config
        String endPoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String endPointPublic = optionService.getPropertyStringValue(PropertyEnum.END_POINT_PUBLIC);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyStringValue(PropertyEnum.BUCKET_NAME);
        String domain = optionService.getPropertyStringValue(PropertyEnum.OSS_DOMAIN);
        String source = optionService.getPropertyStringValue(PropertyEnum.OSS_SOURCE);
        String styleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_STYLE_RULE);
        String thumbnailStyleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_THUMBNAIL_STYLE_RULE);

        StringBuilder basePath = new StringBuilder(domain);
        basePath.append(bucketName)
                .append(".")
                .append(endPointPublic)
                .append("/");

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
        try {
//            String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String timestamp = String.valueOf(System.currentTimeMillis());
            StringBuilder upFilePath = new StringBuilder();
            if (StringUtils.isNotEmpty(source)) {
                upFilePath.append(source)
                        .append("/");
            }
            upFilePath.append(name)
                    .append(".")
                    .append(extension);

            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());
            log.info(basePath.toString());

            PutObjectResult putObjectResult = ossClient.putObject(bucketName, upFilePath.toString(), file.getInputStream());
            if(putObjectResult==null){
                throw  new FileOperationException("File upload failed!!");
            }
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(name);
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

    /**
     * 上传公式的svg到阿里云
     * @return
     */
    @Override
    public UploadResult uploadStrContent(String content,String strName){

        // Get config
        String endPoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String endPointPublic = optionService.getPropertyStringValue(PropertyEnum.END_POINT_PUBLIC);

        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyStringValue(PropertyEnum.BUCKET_NAME);
        String domain = optionService.getPropertyStringValue(PropertyEnum.OSS_DOMAIN);
        String source = optionService.getPropertyStringValue(PropertyEnum.OSS_SOURCE);

        StringBuilder basePath = new StringBuilder(domain);
        basePath.append(bucketName)
                .append(".")
                .append(endPointPublic)
                .append("/");

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
        try {
            String basename ;//FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension;//FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());

            if (strName!=null){
                basename = strName;
                extension = "";
            }else {
                basename = "imgSvg-"+timestamp+".svg";
                extension = "svg";
            }


            StringBuilder upFilePath = new StringBuilder();
            if (StringUtils.isNotEmpty(source)) {
                upFilePath.append(source)
                        .append("/");
            }
            upFilePath.append(basename);
            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());
            log.info(basePath.toString());

            PutObjectResult putObjectResult = ossClient.putObject(bucketName, upFilePath.toString(),new ByteArrayInputStream(content.getBytes()) );
            if(putObjectResult==null){
                throw  new FileOperationException("File upload failed!!");
            }
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(upFilePath.toString());
//            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(Long.valueOf((content.getBytes().length)));
            log.info("Uploaded file: [{}] successfully", upFilePath.toString());
            return uploadResult;

        }finally {
            ossClient.shutdown();
        }

    }
    /**
     * 通过网络url上传文件
     * @param url
     * @return
     */
    @Override
    public UploadResult upload(String url, String name){
        // Get config
        String endPoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyStringValue(PropertyEnum.BUCKET_NAME);
        String domain = optionService.getPropertyStringValue(PropertyEnum.OSS_DOMAIN);
        String source = optionService.getPropertyStringValue(PropertyEnum.OSS_SOURCE);
        String styleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_STYLE_RULE);
        String thumbnailStyleRule = optionService.getPropertyStringValue(PropertyEnum.OSS_THUMBNAIL_STYLE_RULE);

        StringBuilder basePath = new StringBuilder(domain);
        basePath.append(bucketName)
                .append(".")
                .append(endPoint)
                .append("/");

        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
        try {
            InputStream inputStream = new URL(url).openStream();

//            ObjectMetadata meta = new ObjectMetadata();
//            // 指定上传的内容类型。
//            meta.setContentType("text/plain");
            byte[] bytes = new byte[1024*10];
            boolean flag = true;
//            AppendObjectRequest appendObjectRequest=null;
            Long position=0L;
            AppendObjectResult appendObjectResult=null;
            while (inputStream.read(bytes)!=-1){
                if(flag){
                     appendObjectResult = ossClient.appendObject(
                            new AppendObjectRequest(bucketName, name, new ByteArrayInputStream(bytes)).withPosition(0L));
//                    appendObjectRequest = new AppendObjectRequest(bucketName, name, new ByteArrayInputStream(bytes));
//                    appendObjectRequest.setPosition(0L);
//                    AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
//                    position=appendObjectRequest.getPosition();
                    flag=false;
                }else {
                    Long nextPosition = appendObjectResult.getNextPosition();
                    appendObjectResult = ossClient.appendObject(
                            new AppendObjectRequest(bucketName, name,  new ByteArrayInputStream(bytes))
                                    .withPosition(nextPosition));
                    System.out.println("上传.....");
//                    appendObjectRequest.setInputStream(new ByteArrayInputStream(bytes));
//                    AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
//                    position = appendObjectRequest.getPosition();
                }
            }
            // 通过AppendObjectRequest设置多个参数。

            // 第一次追加。
            // 设置文件的追加位置。


//            PutObjectResult putObjectResult = ossClient.putObject(bucketName, name, inputStream);
//            if(putObjectResult==null){
//                throw  new FileOperationException("File upload failed!!");
//            }
            UploadResult uploadResult = new UploadResult();
            return uploadResult;
        } catch (IOException e) {
            throw  new FileOperationException("通过url上传文件失败");
        }finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void delete(String key) {
        Assert.notNull(key, "File key must not be blank");

        // Get config
        String endPoint = optionService.getPropertyStringValue(PropertyEnum.END_POINT);
        String accessKey = optionService.getPropertyStringValue(PropertyEnum.ACCESS_KEY);
        String accessSecret = optionService.getPropertyStringValue(PropertyEnum.ACCESS_SECRET);
        String bucketName = optionService.getPropertyStringValue(PropertyEnum.BUCKET_NAME);

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


