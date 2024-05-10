package com.hyperhire.whatsapp.dto.response.paged;

import com.hyperhire.whatsapp.dto.response.ChatRoomResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class PagedChatRoomResponseDto {
    private List<ChatRoomResponseDto> chatRooms;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PagedChatRoomResponseDto(List<ChatRoomResponseDto> chatRooms, long totalElements, int totalPages, int currentPage, int pageSize) {
        this.chatRooms = chatRooms;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}