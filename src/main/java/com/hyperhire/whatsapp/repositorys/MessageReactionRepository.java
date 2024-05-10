package com.hyperhire.whatsapp.repositorys;

import com.hyperhire.whatsapp.entity.MessageReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    Page<MessageReaction> findByMessageId(Long messageId, Pageable pageable);
    List<MessageReaction> findByMessageId(Long messageId);
}
