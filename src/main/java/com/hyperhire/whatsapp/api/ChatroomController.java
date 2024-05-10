package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.dto.request.ChatroomDTO;
import com.hyperhire.whatsapp.dto.response.ChatRoomResponseDto;
import com.hyperhire.whatsapp.dto.response.paged.PagedChatRoomResponseDto;
import com.hyperhire.whatsapp.entity.Chatroom;
import com.hyperhire.whatsapp.services.ChatroomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;

    @Operation(summary = "Create new chat room. if is group is false that means its a Direct message. if true its a group message")
    @PostMapping
    public ResponseEntity<Chatroom> createChatroom(@RequestBody Chatroom chatroom) {
        Chatroom createdChatroom = chatroomService.createChatroom(chatroom);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChatroom);
    }

    @Operation(summary = "Get All available chat rooms")
    @GetMapping
    public ResponseEntity<PagedChatRoomResponseDto> getAllChatroom(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ChatRoomResponseDto> chatRoomsPage = chatroomService.getAllChatrooms(page, size);
        List<ChatRoomResponseDto> chatRooms = chatRoomsPage.getContent();
        long totalElements = chatRoomsPage.getTotalElements();
        int totalPages = chatRoomsPage.getTotalPages();

        PagedChatRoomResponseDto responseDto = new PagedChatRoomResponseDto(chatRooms, totalElements, totalPages, page, size);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get single chat room by chat room id")
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getSingleChatroom(@PathVariable Long roomId) {
        var chatroom = chatroomService.getChatroomById(roomId);
        if (chatroom != null) {
            return ResponseEntity.ok(chatroom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "update chat room by chat room id.")
    @PutMapping("/{roomId}")
    public ResponseEntity<Chatroom> updateChatroom(@PathVariable Long roomId, @RequestBody ChatroomDTO updatedChatroomDTO) {
        Chatroom chatroom = chatroomService.updateChatroom(roomId, updatedChatroomDTO);
        if (chatroom != null) {
            return ResponseEntity.ok(chatroom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "update chat room status by chat room id.  status's can be ACTIVE, INACTIVE, ARCHIVED")
    @PutMapping("/{roomId}/status")
    public ResponseEntity<Chatroom> updateChatroomStatus(@PathVariable Long roomId, @RequestParam String status) {
        Chatroom chatroom = chatroomService.updateChatroomStatus(roomId, status);
        if (chatroom != null) {
            return ResponseEntity.ok(chatroom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}