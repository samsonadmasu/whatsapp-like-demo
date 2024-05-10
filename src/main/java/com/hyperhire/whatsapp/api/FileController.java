package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.entity.Attachment;
import com.hyperhire.whatsapp.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attachments")
public class FileController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "Upload file to a message")
    @PostMapping("/upload/{messageId}")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @PathVariable Long messageId) {
        try {
            String fileName = fileService.uploadFile(file, messageId);
            return ResponseEntity.ok("File uploaded successfully. File name: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }

    @Operation(summary = "Download file by file name")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = fileService.downloadFile(fileName);
            return ResponseEntity.ok().body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get list of attachments by message id")
    @GetMapping("/message/{messageId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByMessageId(
            @PathVariable Long messageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attachment> attachmentPage = fileService.getAttachmentsByMessageId(messageId, pageable);

        List<Attachment> attachments = attachmentPage.getContent();
        long totalAttachments = attachmentPage.getTotalElements();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(totalAttachments))
                .body(attachments);
    }

    @Operation(summary = "Get attachment by attachment id")
    @GetMapping("/{attachmentId}")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable Long attachmentId) {
        Optional<Attachment> optionalAttachment = fileService.getAttachmentById(attachmentId);
        if (optionalAttachment.isPresent()) {
            return ResponseEntity.ok(optionalAttachment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Download attachment by attachment id")
    @GetMapping("/downloadFile/{attachmentId}")
    public void downloadAttachmentById(@PathVariable Long attachmentId, HttpServletResponse response) throws IOException {
        Optional<String> optionalFilePath = fileService.getFilePathById(attachmentId);
        if (optionalFilePath.isPresent()) {
            String filePath = optionalFilePath.get();
            byte[] fileContent = fileService.getFileContentByFilePath(filePath);

            // Set the content type and header attributes
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + filePath);

            // Write file content to the response output stream
            response.getOutputStream().write(fileContent);
            response.getOutputStream().flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}