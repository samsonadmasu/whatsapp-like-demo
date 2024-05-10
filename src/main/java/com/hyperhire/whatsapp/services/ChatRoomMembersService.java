package com.hyperhire.whatsapp.services;

import com.hyperhire.whatsapp.dto.request.AddMemberToChatRoomRequestDto;
import com.hyperhire.whatsapp.dto.request.ChatRoomMemberDTO;
import com.hyperhire.whatsapp.dto.response.ChatRoomMemberResponseDto;
import com.hyperhire.whatsapp.dto.response.ChatRoomResponseDto;
import com.hyperhire.whatsapp.dto.response.UserResponseDto;
import com.hyperhire.whatsapp.entity.ChatRoomMember;
import com.hyperhire.whatsapp.entity.Chatroom;
import com.hyperhire.whatsapp.entity.User;
import com.hyperhire.whatsapp.repositorys.ChatroomMemberRepository;
import com.hyperhire.whatsapp.repositorys.ChatroomRepository;
import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomMembersService {
    private final ModelMapper modelMapper;

    private final ChatroomMemberRepository chatRoomMemberRepository;
    private final UserProfileService userProfileService;
    private final ChatroomRepository chatroomRepository;

    public ChatRoomMembersService(ModelMapper modelMapper, ChatroomMemberRepository chatRoomMemberRepository, UserProfileService userProfileService, ChatroomRepository chatroomRepository) {
        this.modelMapper = modelMapper;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.userProfileService = userProfileService;
        this.chatroomRepository = chatroomRepository;
    }

    public ChatRoomMember createChatRoomMember(AddMemberToChatRoomRequestDto chatRoomMember) {
        User userById = userProfileService.getUserById(chatRoomMember.getUserId());
        ChatRoomMember newChatRoomMember = new ChatRoomMember();
        newChatRoomMember.setUser(userById);
        newChatRoomMember.setChatroom(chatroomRepository.findById(chatRoomMember.getChatroomId()).orElse(null));
        newChatRoomMember.setStatus(MemberStatus.valueOf(chatRoomMember.getMemberStatus()));
        newChatRoomMember.setCreatedDate(LocalDateTime.now());
        newChatRoomMember.setUpdatedDate(LocalDateTime.now());
        // Implement logic to create a new chat room member
        return chatRoomMemberRepository.save(newChatRoomMember);
    }

    public ChatRoomMember updateChatRoomMember(Long id, ChatRoomMemberDTO updatedMemberDTO) {
        ChatRoomMember existingMember = getChatRoomMemberById(id);
        if (existingMember != null) {
            // Update the properties of the existing member with values from the DTO
            existingMember.setStatus(updatedMemberDTO.getStatus());
            // You may need to update other properties here as well

            // Save the updated member
            return chatRoomMemberRepository.save(existingMember);
        }
        return null; // Return null if the member doesn't exist
    }

    public void deleteChatRoomMember(Long id) {
        chatRoomMemberRepository.deleteById(id);
    }

    public ChatRoomMember changeMemberStatus(Long id, MemberStatus newStatus) {
        ChatRoomMember existingMember = getChatRoomMemberById(id);
        if (existingMember != null) {
            existingMember.setStatus(newStatus);
            return chatRoomMemberRepository.save(existingMember);
        }
        return null;
    }

    public List<ChatRoomMemberResponseDto> getMembersByChatRoom(Long chatroomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoomMember> chatRoomMembersPage = chatRoomMemberRepository.findByChatroomId(chatroomId, pageable);
        return chatRoomMembersPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ChatRoomMemberResponseDto> getAllMembersByChatRoomId(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoomMember> chatRoomMembersPage = chatRoomMemberRepository.findByChatroomId(chatRoomId, pageable);

        return chatRoomMembersPage.map(this::convertToResponseDto);
    }

    private ChatRoomMemberResponseDto convertToResponseDto(ChatRoomMember chatRoomMember) {
//        ChatRoomMemberResponseDto responseDto = modelMapper.map(chatRoomMember, ChatRoomMemberResponseDto.class);
        ChatRoomMemberResponseDto responseDto = ChatRoomMemberResponseDto.builder()
                .id(chatRoomMember.getId())
                .status(chatRoomMember.getStatus())
                .user(getUserDtoById(chatRoomMember.getUser().getId()))
                .chatroom(getChatRoomDtoById(chatRoomMember.getChatroom().getId()))
                .build();
        responseDto.setUser(getUserDtoById(chatRoomMember.getUser().getId()));
        responseDto.setChatroom(getChatRoomDtoById(chatRoomMember.getChatroom().getId()));
        return responseDto;
    }

    public ChatRoomMember getChatRoomMemberById(Long id) {
        return chatRoomMemberRepository.findById(id)
                .orElse(null);
    }

    public UserResponseDto getUserDtoById(Long userId) {
        // Retrieve user from repository or service
        User user = userProfileService.getUserById(userId);
        return UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .updatedDate(user.getUpdatedDate())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public ChatRoomResponseDto getChatRoomDtoById(Long chatRoomId) {
        // Retrieve chatroom from repository or service
        Chatroom chatroom = chatroomRepository.findById(chatRoomId).orElseThrow(() -> new EntityNotFoundException("Chatroom not found"));

        // Convert Chatroom entity to ChatRoomDto
        ChatRoomResponseDto responseDto = new ChatRoomResponseDto();
        responseDto.setId(chatroom.getId());
        responseDto.setName(chatroom.getName());
        responseDto.setGroup(chatroom.isGroup());
        responseDto.setStatus(chatroom.getStatus());
        responseDto.setCreatedDate(chatroom.getCreatedDate());
        responseDto.setUpdatedDate(chatroom.getUpdatedDate());
        return responseDto;
    }
}
