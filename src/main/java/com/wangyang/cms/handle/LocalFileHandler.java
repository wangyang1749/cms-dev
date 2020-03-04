package com.wangyang.cms.handle;

import com.wangyang.cms.expection.FileOperationException;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.UploadResult;
import com.wangyang.cms.utils.CMSUtils;
import com.wangyang.cms.utils.FilenameUtils;
import com.wangyang.cms.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class LocalFileHandler implements FileHandler{
    @Value("${cms.workDir}")
    private String workDir;
    @Value("${cms.uploadPrefix}")
    private String uploadPrefix;
    private final static String UPLOAD_SUB_DIR = "/upload/";
    private final static String THUMBNAIL_SUFFIX = "-thumbnail";
    private final static int THUMB_WIDTH = 256;
    private final static int THUMB_HEIGHT = 256;
    ReentrantLock lock = new ReentrantLock();


    @Override
    public boolean supportType(AttachmentType type) {
        return AttachmentType.LOCAL.equals(type);
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");
        // Get current time
        // Get month and day of month
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month =date.get(Calendar.MONTH);

        // Build directory /upload/2020/2/
        String subDir = UPLOAD_SUB_DIR + year + File.separator + month + File.separator;
        //去掉后缀 eg.Screenshot from 2020-02-28 15-43-32
        String originalBasename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));

        // Get basename
        String basename = originalBasename + '-' + CMSUtils.randomViewName();

        // Get extension eg. png
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        log.debug("Base name: [{}], extension: [{}] of original filename: [{}]", basename, extension, file.getOriginalFilename());

        // Build sub file path /upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
        String subFilePath = subDir + basename + '.' + extension;

        // Get upload path /home/wy/cms/upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
        Path uploadPath = Paths.get(workDir, subFilePath);

        log.info("Uploading file: [{}]to directory: [{}]", file.getOriginalFilename(), uploadPath.toString());

        try {
            // TODO Synchronize here
            // 创建目录
            Files.createDirectories(uploadPath.getParent());
            //创建文件
            Files.createFile(uploadPath);

            // 上传文件
            file.transferTo(uploadPath);

            // Build upload result
            UploadResult uploadResult = new UploadResult();
            //Screenshot from 2020-02-28 15-43-32
            uploadResult.setFilename(originalBasename);
            ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
            if(uploadPrefix!=null){
                uploadResult.setFilePath(uploadPrefix+subFilePath); //
            }else {
                uploadResult.setFilePath(subFilePath); //

            }
            ///upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f.png
            uploadResult.setKey(subFilePath);
            //png
            uploadResult.setSuffix(extension);
            //image/png
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSize(file.getSize());

            // TODO refactor this: if image is svg ext. extension
            boolean isSvg = "svg".equals(extension);

            // Check file type
            if (FileHandler.isImageType(uploadResult.getMediaType()) && !isSvg) {
                lock.lock();
                try (InputStream uploadFileInputStream = new FileInputStream(uploadPath.toFile())) {
                    // Upload a thumbnail /upload/2020/2/Screenshot from 2020-02-28 15-43-32-2015c76b-9442-435a-a1b7-ad030548d57f-thumbnail.png
                    String thumbnailBasename = basename + THUMBNAIL_SUFFIX;
                    String thumbnailSubFilePath = subDir + thumbnailBasename + '.' + extension;
                    Path thumbnailPath = Paths.get(workDir + thumbnailSubFilePath);

                    // Read as image
                    BufferedImage originalImage = ImageUtils.getImageFromFile(uploadFileInputStream, extension);
                    // Set width and height
                    uploadResult.setWidth(originalImage.getWidth());
                    uploadResult.setHeight(originalImage.getHeight());

                    // Generate thumbnail
                    boolean result = generateThumbnail(originalImage, thumbnailPath, extension);
                    if (result) {
                        // Set thumb path
                        uploadResult.setThumbPath(thumbnailSubFilePath);
                    } else {
                        // If generate error
                        uploadResult.setThumbPath(subFilePath);
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                uploadResult.setThumbPath(subFilePath);
            }

            log.info("Uploaded file: [{}] to directory: [{}] successfully", file.getOriginalFilename(), uploadPath.toString());
            return uploadResult;
        } catch (IOException e) {
            throw new FileOperationException("上传附件失败").setErrorData(uploadPath);
        }
    }

    @Override
    public void delete(String key) {
        Assert.hasText(key, "File key must not be blank");
        // Get path
        Path path = Paths.get(workDir, key);

        // Delete the file key
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new FileOperationException("附件 " + key + " 删除失败");
        }

        // Delete thumb if necessary
        String basename = FilenameUtils.getBasename(key);
        String extension = FilenameUtils.getExtension(key);

        // Get thumbnail name
        String thumbnailName = basename + THUMBNAIL_SUFFIX + '.' + extension;

        // Get thumbnail path
        Path thumbnailPath = Paths.get(path.getParent().toString(), thumbnailName);

        // Delete thumbnail file
        try {
            boolean deleteResult = Files.deleteIfExists(thumbnailPath);
            if (!deleteResult) {
                log.warn("Thumbnail: [{}] may not exist", thumbnailPath.toString());
            }
        } catch (IOException e) {
            throw new FileOperationException("附件缩略图 " + thumbnailName + " 删除失败");
        }
    }


    private boolean generateThumbnail(BufferedImage originalImage, Path thumbPath, String extension) {
        Assert.notNull(originalImage, "Image must not be null");
        Assert.notNull(thumbPath, "Thumb path must not be null");


        boolean result = false;
        // Create the thumbnail
        try {
            Files.createFile(thumbPath);
            // Convert to thumbnail and copy the thumbnail
            log.debug("Trying to generate thumbnail: [{}]", thumbPath.toString());
            Thumbnails.of(originalImage).size(THUMB_WIDTH, THUMB_HEIGHT).keepAspectRatio(true).toFile(thumbPath.toFile());
            log.debug("Generated thumbnail image, and wrote the thumbnail to [{}]", thumbPath.toString());
            result = true;
        } catch (Throwable t) {
            log.warn("Failed to generate thumbnail: " + thumbPath, t);
        }
        return result;
    }

}
