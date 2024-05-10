package com.hyperhire.whatsapp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatroomDTO {
    private String name;
    private String status;
}
