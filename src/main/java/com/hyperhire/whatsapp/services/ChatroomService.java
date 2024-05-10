package com.hyperhire.whatsapp.services;

import com.hyperhire.whatsapp.dto.request.ChatroomDTO;
import com.hyperhire.whatsapp.dto.response.ChatRoomResponseDto;
import com.hyperhire.whatsapp.entity.Chatroom;
import com.hyperhire.whatsapp.repositorys.ChatroomRepository;
import com.hyperhire.whatsapp.shared.enums.ChatroomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;

    @Autowired
    public ChatroomService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
    }

    public Page<ChatRoomResponseDto> getAllChatrooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chatroom> chatroomPage = chatroomRepository.findAll(pageable);
        return chatroomPage.map(this::convertToResponseDto);
    }

    public ChatRoomResponseDto convertToResponseDto(Chatroom chatroom) {
        ChatRoomResponseDto responseDto = new ChatRoomResponseDto();
        responseDto.setId(chatroom.getId());
        responseDto.setName(chatroom.getName());
        responseDto.setGroup(chatroom.isGroup());
        responseDto.setStatus(chatroom.getStatus());
        responseDto.setCreatedDate(chatroom.getCreatedDate());
        responseDto.setUpdatedDate(chatroom.getUpdatedDate());
        return responseDto;
    }

    public ChatRoomResponseDto getChatroomById(Long id) {
        var chatroom = chatroomRepository.findById(id).orElse(null);
        var chatRoomResponseDto = convertToResponseDto(chatroom);
        return chatRoomResponseDto;
    }

    public Chatroom findChatroomById(Long id) {
        var chatroom = chatroomRepository.findById(id).orElse(null);
        return chatroom;
    }

    public Chatroom createChatroom(Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }

    public Chatroom updateChatroom(Long roomId, ChatroomDTO updatedChatroomDTO) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(roomId);
        if (optionalChatroom.isPresent()) {
            Chatroom chatroom = optionalChatroom.get();
            chatroom.setName(updatedChatroomDTO.getName());
            chatroom.setStatus(ChatroomStatus.valueOf(updatedChatroomDTO.getStatus()));
            return chatroomRepository.save(chatroom);
        } else {
            return null; // Chatroom not found
        }
    }

    public Chatroom updateChatroomStatus(Long roomId, String status) {
        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(roomId);
        if (optionalChatroom.isPresent()) {
            Chatroom chatroom = optionalChatroom.get();
            // Update the status
            chatroom.setStatus(ChatroomStatus.valueOf(status));
            // Save the updated chatroom
            return chatroomRepository.save(chatroom);
        } else {
            return null; // Chatroom not found
        }
    }
}
