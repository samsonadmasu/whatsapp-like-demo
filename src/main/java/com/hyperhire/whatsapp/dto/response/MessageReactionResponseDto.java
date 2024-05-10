package com.hyperhire.whatsapp.dto.response;

import com.hyperhire.whatsapp.shared.enums.EmojiType;
import lombok.Data;

@Data
public class MessageReactionResponseDto {
    private Long id;
    private MessageResponseDto message;
    private UserResponseDto user;
    private EmojiType reactionType;
}
