package com.hyperhire.whatsapp.dto.response;

import com.hyperhire.whatsapp.shared.enums.EmojiType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReactionResponseDto {
    private Long id;
    private EmojiType reactionType;
    private UserResponseDto user;
}
