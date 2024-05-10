package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.dto.request.AddMemberToChatRoomRequestDto;
import com.hyperhire.whatsapp.dto.request.ChatRoomMemberDTO;
import com.hyperhire.whatsapp.dto.response.ChatRoomMemberResponseDto;
import com.hyperhire.whatsapp.dto.response.paged.ChatRoomMemberPageDto;
import com.hyperhire.whatsapp.entity.ChatRoomMember;
import com.hyperhire.whatsapp.services.ChatRoomMembersService;
import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatRoomMembers")
public class ChatRoomMembersController {
    @Autowired
    private ChatRoomMembersService chatRoomMemberService;

    @Operation(summary = "Create new chat room member (add member to the chat room) if the member is not already in the chat room.")
    @PostMapping
    public ResponseEntity<ChatRoomMember> createChatRoomMember(@RequestBody AddMemberToChatRoomRequestDto chatRoomMember) {
        ChatRoomMember createdMember = chatRoomMemberService.createChatRoomMember(chatRoomMember);
        return ResponseEntity.ok(createdMember);
    }

    @Operation(summary = "Update chat room member status. and status's can be ACTIVE, INACTIVE")
    @PutMapping("/{id}")
    public ResponseEntity<ChatRoomMemberDTO> updateChatRoomMember(@PathVariable Long id, @RequestBody ChatRoomMemberDTO updatedMemberDTO) {
        ChatRoomMember updatedMember = chatRoomMemberService.updateChatRoomMember(id, updatedMemberDTO);
        ChatRoomMemberDTO updatedMemberResponse = ChatRoomMemberDTO.builder()
                .status(updatedMember.getStatus())
                .build();
        return ResponseEntity.ok(updatedMemberResponse);
    }

    @Operation(summary = "Delete or remove member from the chat room")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChatRoomMember(@PathVariable Long id) {
        chatRoomMemberService.deleteChatRoomMember(id);
        return ResponseEntity.ok("Chat room member deleted successfully");
    }

    @Operation(summary = "Update member status from ACTIVE to INACTIVE or vice versa")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ChatRoomMember> changeMemberStatus(@PathVariable Long id, @RequestBody MemberStatus newStatus) {
        ChatRoomMember updatedMember = chatRoomMemberService.changeMemberStatus(id, newStatus);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "Get chat room detail by chat room id")
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<ChatRoomMemberPageDto> getMembersByChatRoomId(@PathVariable Long chatRoomId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Page<ChatRoomMemberResponseDto> membersPage = chatRoomMemberService.getAllMembersByChatRoomId(chatRoomId, page, size);

        // Create a custom pagination object
        ChatRoomMemberPageDto pageDto = new ChatRoomMemberPageDto();
        pageDto.setContent(membersPage.getContent());
        pageDto.setTotalElements(membersPage.getTotalElements());
        pageDto.setTotalPages(membersPage.getTotalPages());
        pageDto.setPageNumber(membersPage.getNumber());
        pageDto.setPageSize(membersPage.getSize());
        return ResponseEntity.ok(pageDto);
    }
}
