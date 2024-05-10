package com.hyperhire.whatsapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class AttachmentResponseDto {
    private Long id;
    private Long messageId;
    private String fileName;
    private String filePath;
    private String fileType;
}
