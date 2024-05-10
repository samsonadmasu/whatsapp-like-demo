package com.hyperhire.whatsapp.repositorys;

import com.hyperhire.whatsapp.entity.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findByMessageId(Long messageId, Pageable pageable);
    List<Attachment> findByMessageId(Long messageId);
    Optional<Attachment> findById(Long attachmentId);
}
