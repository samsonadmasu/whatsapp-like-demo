package com.hyperhire.whatsapp.repositorys;

import com.hyperhire.whatsapp.entity.ChatRoomMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    Page<ChatRoomMember> findByChatroomId(Long chatroomId, Pageable pageable);
}
