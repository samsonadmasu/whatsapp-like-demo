package com.hyperhire.whatsapp.entity;
import com.hyperhire.whatsapp.shared.enums.MemberStatus;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_room_members")
@Data
public class ChatRoomMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Enumerated(EnumType.STRING) // Map the enum as string
    @Column(nullable = false)
    private MemberStatus status; // Field to store the member status in the chat room

}