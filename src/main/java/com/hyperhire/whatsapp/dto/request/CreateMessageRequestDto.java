package com.hyperhire.whatsapp.dto.request;

import lombok.Data;

@Data
public class CreateMessageRequestDto {
    private Long userId;
    private Long chatroomId;
    private String content;
    private Long replyToId;
    private String status;
}
