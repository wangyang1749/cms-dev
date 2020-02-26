package com.wangyang.cms.handle;

import com.wangyang.cms.expection.FileOperationException;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.UploadResult;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.LinkedList;

@Component
public class FileHandlers {

    /**
     * File handler container.
     */
    private final Collection<FileHandler> fileHandlers = new LinkedList<>();

    public FileHandlers(ApplicationContext applicationContext) {
        // Add all file handler
        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
    }

    public UploadResult upload(@NonNull MultipartFile file, @NonNull AttachmentType attachmentType) {
        Assert.notNull(file, "Multipart file must not be null");
        Assert.notNull(attachmentType, "Attachment type must not be null");

        for (FileHandler fileHandler : fileHandlers) {
            if (fileHandler.supportType(attachmentType)) {
                return fileHandler.upload(file);
            }
        }

        throw new FileOperationException("No available file handler to upload the file").setErrorData(attachmentType);
    }

    /**
     * Adds file handlers.
     *
     * @param fileHandlers file handler collection
     * @return current file handlers
     */
    @NonNull
    public FileHandlers addFileHandlers(@Nullable Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            this.fileHandlers.addAll(fileHandlers);
        }
        return this;
    }
}
