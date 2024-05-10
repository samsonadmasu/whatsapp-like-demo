package com.hyperhire.whatsapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageReactionDTO {

    private String reactionType;

    // Getters and setters
}