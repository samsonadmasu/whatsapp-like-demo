package com.hyperhire.whatsapp.dto.response;

import com.hyperhire.whatsapp.dto.request.AttachmentResponseDto;
import com.hyperhire.whatsapp.shared.enums.MessageStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MessageResponseDto {
    private Long id;
    private String content;
    private UserResponseDto user;
    private ChatRoomResponseDto chatRoom;
    private MessageStatus status;
    private List<AttachmentResponseDto> attachments;
    private List<ReactionResponseDto> reactions;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
