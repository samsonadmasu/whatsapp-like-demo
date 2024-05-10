package com.hyperhire.whatsapp.dto.response;

import com.hyperhire.whatsapp.shared.enums.ChatroomStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResponseDto {
    private Long id;
    private String name;
    private boolean isGroup;
    private ChatroomStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
