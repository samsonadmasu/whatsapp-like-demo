package com.hyperhire.whatsapp.dto.response;

import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomMemberResponseDto {
    private Long id;
    private UserResponseDto user;
    private ChatRoomResponseDto chatroom;
    private MemberStatus status;
}
