package com.hyperhire.whatsapp.entity;

import com.hyperhire.whatsapp.shared.enums.MessageStatus;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
@Data
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "reply_to_id", nullable = true)
    private Message replyTo; // Represents the message to which this message is a reply

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status; // Add status field

}
