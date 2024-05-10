package com.hyperhire.whatsapp.services;

import com.hyperhire.whatsapp.dto.request.AttachmentResponseDto;
import com.hyperhire.whatsapp.entity.Attachment;
import com.hyperhire.whatsapp.entity.Message;
import com.hyperhire.whatsapp.repositorys.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Value("${upload.directory}")
    private String uploadDirectory;

    private final AttachmentRepository attachmentRepository;

    private final MessageService messageService;

    public FileService(AttachmentRepository attachmentRepository, MessageService messageService) {
        this.attachmentRepository = attachmentRepository;
        this.messageService = messageService;
    }

    public String uploadFile(MultipartFile file, Long messageId) throws IOException {
        Message message = messageService.getMessageById(messageId);
        // Create the upload directory if it doesn't exist
        createUploadDirectoryIfNotExists();

        // Generate a unique file name
        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        // Save the file to the upload directory
        Path path = Paths.get(uploadDirectory + File.separator + fileName);
        Files.copy(file.getInputStream(), path);

        // Save file details into the database
        Attachment attachment = new Attachment();
        attachment.setMessage(message);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFilePath(fileName);
        attachment.setCreatedDate(LocalDateTime.now());
        attachment.setUpdatedDate(LocalDateTime.now());
        attachmentRepository.save(attachment);

        return fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        // Load the file from the upload directory
        Path path = Paths.get(uploadDirectory + File.separator + fileName);
        return Files.readAllBytes(path);
    }

    private void createUploadDirectoryIfNotExists() {
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public Page<Attachment> getAttachmentsByMessageId(Long messageId, Pageable pageable) {
        return attachmentRepository.findByMessageId(messageId, pageable);
    }

    public Optional<Attachment> getAttachmentById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId);
    }

    public Optional<String> getFilePathById(Long attachmentId) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(attachmentId);
        return optionalAttachment.map(Attachment::getFilePath);
    }

    public byte[] getFileContentByFilePath(String filePath) throws IOException {
        return downloadFile(filePath);
    }


}
