package com.hyperhire.whatsapp.entity;

import com.hyperhire.whatsapp.shared.enums.ChatroomStatus;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "chatrooms")
@Data
public class Chatroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Column(name = "is_group", nullable = false)
    private boolean isGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatroomStatus status;

}
