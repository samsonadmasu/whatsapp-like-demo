package com.hyperhire.whatsapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String email;

    // Constructors, getters, and setters
}
