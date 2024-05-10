package com.hyperhire.whatsapp.dto.response.paged;

import com.hyperhire.whatsapp.dto.response.ChatRoomMemberResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomMemberPageDto {
    private List<ChatRoomMemberResponseDto> content;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
}