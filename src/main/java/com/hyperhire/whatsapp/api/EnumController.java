package com.hyperhire.whatsapp.api;

import com.hyperhire.whatsapp.shared.enums.ChatroomStatus;
import com.hyperhire.whatsapp.shared.enums.EmojiType;
import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import com.hyperhire.whatsapp.shared.enums.MessageStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/enums")
public class EnumController {

    @Operation(summary = "Return list of available emojis/reactions")
    @GetMapping("/emojis")
    public ResponseEntity<List<String>> getEmojis() {
        List<String> emojis = Arrays.asList(
                EmojiType.THUMB_UP.name(),
                EmojiType.LOVE.name(),
                EmojiType.CRYING.name(),
                EmojiType.SURPRISED.name()
        );
        return ResponseEntity.ok(emojis);
    }

    @Operation(summary = "Return list of available chat room member status")
    @GetMapping("/chat-room-member-status")
    public ResponseEntity<List<String>> getChatRoomMemberStatus() {
        List<String> memberStatus = Arrays.asList(
                MemberStatus.ACTIVE.name(),
                MemberStatus.INACTIVE.name()
        );
        return ResponseEntity.ok(memberStatus);
    }

    @Operation(summary = "Return list of available chat room status")
    @GetMapping("/chat-room-status")
    public ResponseEntity<List<String>> getChatRoomStatus() {
        List<String> roomStatus = Arrays.asList(
                ChatroomStatus.ACTIVE.name(),
                ChatroomStatus.INACTIVE.name(),
                ChatroomStatus.ARCHIVED.name()
        );
        return ResponseEntity.ok(roomStatus);
    }

    @Operation(summary = "Return list of available message status")
    @GetMapping("/message-status")
    public ResponseEntity<List<String>> getMessageStatus() {
        List<String> messageStatus = Arrays.asList(
                MessageStatus.SENT.name(),
                MessageStatus.RECEIVED.name(),
                MessageStatus.READ.name(),
                MessageStatus.DELETED.name()
        );
        return ResponseEntity.ok(messageStatus);
    }
}
