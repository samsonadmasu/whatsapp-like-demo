package com.hyperhire.whatsapp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
