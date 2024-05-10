package com.hyperhire.whatsapp.repositorys;

import com.hyperhire.whatsapp.entity.Chatroom;
import com.hyperhire.whatsapp.shared.enums.ChatroomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    Page<Chatroom> findAll(Pageable pageable);
}
