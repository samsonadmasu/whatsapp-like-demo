package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.dto.request.MessageReactionDTO;
import com.hyperhire.whatsapp.dto.response.MessageReactionResponseDto;
import com.hyperhire.whatsapp.services.MessageReactionService;
import com.hyperhire.whatsapp.shared.enums.EmojiType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messagesReaction")
public class MessageReactionController {

    private final MessageReactionService messageReactionService;

    @Autowired
    public MessageReactionController(MessageReactionService messageReactionService) {
        this.messageReactionService = messageReactionService;
    }

    @Operation(summary = "Post a reaction to a message")
    @PostMapping("/{messageId}")
    public ResponseEntity<String> reactToMessage(
            @PathVariable Long messageId,
            @RequestParam String emojiType,
            @RequestParam Long userId) {
        try {
            EmojiType emojiTypeEnum = EmojiType.valueOf(emojiType.toUpperCase()); // Convert the string to enum
            messageReactionService.reactToMessage(messageId, String.valueOf(emojiTypeEnum), userId);
            return ResponseEntity.ok("Message reacted successfully");
        } catch (IllegalArgumentException e) {
            // Handle the case where the provided emoji type is invalid
            return ResponseEntity.badRequest().body("Invalid emoji type");
        } catch (Exception e) {
            throw new RuntimeException("Invalid Request: " + e.getMessage());
        }
    }

    @Operation(summary = "get individual reaction by id")
    @GetMapping("/{id}")
    public MessageReactionResponseDto getMessageReactionById(@PathVariable Long id) {
        try {
            var messageReaction = messageReactionService.getMessageReactionById(id);
            return messageReaction;
        } catch (Exception e) {
            return null;
        }
    }

    @Operation(summary = "get all message reactions by message id")
    @GetMapping("/reactions/{messageId}")
    public List<MessageReactionResponseDto> getMessageReactionsByMessageId(@PathVariable Long messageId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        try {
            var messageReactions = messageReactionService.getMessageReactionsByMessageId(messageId, page, size);
            return messageReactions;
        } catch (Exception e) {
            return null;
        }
    }

    @Operation(summary = "update message reaction")
    @PutMapping("/{reactionId}")
    public ResponseEntity<String> updateMessageReaction(
            @PathVariable Long reactionId,
            @RequestBody MessageReactionDTO updatedReaction) {
        try {
            messageReactionService.updateMessageReaction(reactionId, updatedReaction);
            return ResponseEntity.ok("Message reaction updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating message reaction: " + e.getMessage());
        }
    }

    @Operation(summary = "delete reaction from a message")
    @DeleteMapping("/{reactionId}")
    public ResponseEntity<String> deleteMessageReaction(@PathVariable Long reactionId) {
        try {
            messageReactionService.deleteMessageReaction(reactionId);
            return ResponseEntity.ok("Message reaction deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting message reaction: " + e.getMessage());
        }
    }
}
