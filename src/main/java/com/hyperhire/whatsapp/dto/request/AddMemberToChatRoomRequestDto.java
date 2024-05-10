package com.hyperhire.whatsapp.dto.request;

import lombok.Data;

@Data
public class AddMemberToChatRoomRequestDto {
    private Long userId;
    private Long chatroomId;
    private String memberStatus;
}
