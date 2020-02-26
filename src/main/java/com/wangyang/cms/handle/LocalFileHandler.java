package com.wangyang.cms.handle;

import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class LocalFileHandler implements FileHandler{
    @Value("${cms.workDir}")
    private String workDir;
    @Override
    public boolean supportType(AttachmentType type) {
        return AttachmentType.LOCAL.equals(type);
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        try {

            // Get current time
            Calendar current = Calendar.getInstance();
            // Get month and day of month
            int year = current.get(Calendar.YEAR);
            int month = current.get(Calendar.MONTH) + 1;

            // Build directory
            String subDir = CmsConst.UPLOAD_SUB_DIR + year + File.separator + month + File.separator;

            String originalFilename = file.getOriginalFilename();
//            String[] nameArray = originalFilename.split(".");
//            String baseName = nameArray[0]+"-"+ UUID.randomUUID();
//            String extension = nameArray[1];


//            log.debug("Base name: [{}], extension: [{}] of original filename: [{}]", baseName, extension, file.getOriginalFilename());

            // Build sub file path
//            String subFilePath = subDir + baseName + '.' + extension;
            String subFilePath = subDir+UUID.randomUUID()+"-"+originalFilename;
            // Get upload path
            Path uploadPath = Paths.get(workDir, subFilePath);

            // TODO Synchronize here
            // Create directory
            Files.createDirectories(uploadPath.getParent());
            Files.createFile(uploadPath);

            // Upload this file
            file.transferTo(uploadPath);

            // Build upload result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(originalFilename);
            uploadResult.setFilePath(subFilePath);
            uploadResult.setKey(subFilePath);
//            uploadResult.setSuffix(extension);
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSize(file.getSize());

            return uploadResult;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
