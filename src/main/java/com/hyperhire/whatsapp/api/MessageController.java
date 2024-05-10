package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.dto.request.CreateMessageRequestDto;
import com.hyperhire.whatsapp.dto.request.MessageDTO;
import com.hyperhire.whatsapp.dto.response.MessageResponseDto;
import com.hyperhire.whatsapp.dto.response.paged.PagedResponse;
import com.hyperhire.whatsapp.entity.Message;
import com.hyperhire.whatsapp.services.ChatroomService;
import com.hyperhire.whatsapp.services.MessageService;
import com.hyperhire.whatsapp.services.UserProfileService;
import com.hyperhire.whatsapp.shared.enums.MessageStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ChatroomService chatroomService;

    @Operation(summary = "Post a new message in the chatroom")
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequestDto message) {
        Message requestMsg = new Message();
        try {
            // Perform input validation
            if (message == null || message.getContent() == null || message.getContent().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Retrieve the sender user
            var sender = userProfileService.getUserById(message.getUserId());
            // Retrieve the chatroom
            var chatroom = chatroomService.findChatroomById(message.getChatroomId());
            // Validate replyto
            Message replyTo = null;
            if (message.getReplyToId() != null) {
                replyTo = messageService.getMessageById(message.getReplyToId());
            } else {
                message.setReplyToId(null);
            }

            // Set the sender and chatroom for the message
            requestMsg.setUser(sender);
            requestMsg.setChatroom(chatroom);
            requestMsg.setContent(message.getContent());
            requestMsg.setReplyTo(replyTo);
            requestMsg.setCreatedDate(LocalDateTime.now());
            requestMsg.setUpdatedDate(LocalDateTime.now());
            requestMsg.setStatus(message.getStatus() != null ? MessageStatus.valueOf(message.getStatus()) : MessageStatus.SENT);
            // Save the message
            Message savedMessage = messageService.sendMessage(requestMsg);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
        } catch (Exception e) {
            // Handle any unexpected errors and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get all messages in the chatroom")
    @GetMapping()
    public ResponseEntity<PagedResponse<MessageResponseDto>> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MessageResponseDto> messagesPage = messageService.getMessages(page, size);
        List<MessageResponseDto> messagesContent = messagesPage.getContent();
        PagedResponse<MessageResponseDto> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(messagesContent);
        pagedResponse.setTotalPages(messagesPage.getTotalPages());
        pagedResponse.setPageNumber(messagesPage.getNumber());
        pagedResponse.setPageSize(messagesPage.getSize());

        return ResponseEntity.ok(pagedResponse);
    }

    @Operation(summary = "Update message")
    @PutMapping("/{messageId}")
    public ResponseEntity<String> updateMessage(
            @PathVariable Long messageId,
            @RequestBody MessageDTO updatedMessage) {
        try {
            messageService.updateMessage(messageId, updatedMessage);
            return ResponseEntity.ok("Message updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating message: " + e.getMessage());
        }
    }
}