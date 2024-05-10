package com.hyperhire.whatsapp.dto.request;

import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatRoomMemberDTO {
    private MemberStatus status;
}
