package com.hyperhire.whatsapp.entity;

import com.hyperhire.whatsapp.shared.enums.EmojiType;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "message_reactions")
@Data
public class MessageReaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) // Map the enum as string
    @Column(name = "reaction_type", nullable = false)
    private EmojiType reactionType; // Field to store the type of emoji reaction
}

