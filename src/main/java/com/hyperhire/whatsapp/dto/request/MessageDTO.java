package com.hyperhire.whatsapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    private String content;
    private String status;
}
