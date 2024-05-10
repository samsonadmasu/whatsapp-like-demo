package com.hyperhire.whatsapp.services;

import com.hyperhire.whatsapp.dto.request.AttachmentResponseDto;
import com.hyperhire.whatsapp.dto.request.MessageDTO;
import com.hyperhire.whatsapp.dto.response.MessageResponseDto;
import com.hyperhire.whatsapp.dto.response.ReactionResponseDto;
import com.hyperhire.whatsapp.entity.Attachment;
import com.hyperhire.whatsapp.entity.Message;
import com.hyperhire.whatsapp.entity.MessageReaction;
import com.hyperhire.whatsapp.repositorys.AttachmentRepository;
import com.hyperhire.whatsapp.repositorys.MessageReactionRepository;
import com.hyperhire.whatsapp.repositorys.MessageRepository;
import com.hyperhire.whatsapp.shared.enums.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomMembersService chatRoomMembersService;
    private final ChatroomService chatroomService;
    private final AttachmentRepository attachmentRepository;
    private final MessageReactionRepository messageReactionRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRoomMembersService chatRoomMembersService, ChatroomService chatroomService, AttachmentRepository attachmentRepository,MessageReactionRepository messageReactionRepository) {
        this.messageRepository = messageRepository;
        this.chatRoomMembersService = chatRoomMembersService;
        this.attachmentRepository = attachmentRepository;
        this.chatroomService = chatroomService;
        this.messageReactionRepository = messageReactionRepository;
    }

    public Message sendMessage(Message message) {
        // Save the message to the database
        return messageRepository.save(message);
    }


    public Page<MessageResponseDto> getMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageRepository.findAll(pageable);

        // Convert each Message entity to MessageResponseDto
        return messagePage.map(this::convertToResponseDto);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void updateMessage(Long messageId, MessageDTO updatedMessage) {
        // Retrieve the existing message from the database
        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message with ID " + messageId + " not found"));

        // Update the fields with the new values from the DTO
        existingMessage.setContent(updatedMessage.getContent());
        existingMessage.setStatus(MessageStatus.valueOf(updatedMessage.getStatus()));

        // Save the updated message
        messageRepository.save(existingMessage);
    }

    public MessageResponseDto convertToResponseDto(Message message) {
        var attachment = getAttachment(message.getId());
        var reaction = getReaction(message.getId());
        MessageResponseDto responseDto = new MessageResponseDto();
        responseDto.setId(message.getId());
        responseDto.setContent(message.getContent());
        responseDto.setUser(chatRoomMembersService.getUserDtoById(message.getUser().getId()));
        responseDto.setChatRoom(chatroomService.convertToResponseDto(message.getChatroom()));
        responseDto.setStatus(message.getStatus());
        responseDto.setCreatedOn(message.getCreatedDate());
        responseDto.setUpdatedOn(message.getUpdatedDate());
        responseDto.setAttachments(attachment);
        responseDto.setReactions(reaction);
        return responseDto;
    }

    public List<Attachment> getAttachmentsByMessageId(Long messageId) {
        return attachmentRepository.findByMessageId(messageId);
    }

    public List<AttachmentResponseDto> getAttachment(Long messageId) {
        List<Attachment> attachments = getAttachmentsByMessageId(messageId);

        // Convert each Attachment entity to AttachmentResponseDto
        return attachments.stream()
                .map(this::convertAttachmentToResponseDto)
                .collect(Collectors.toList());
    }

    private List<ReactionResponseDto> getReaction(Long messageId) {
        List<MessageReaction> reactions = messageReactionRepository.findByMessageId(messageId);

        return reactions.stream()
                .map(this::convertReactionToResponseDto)
                .collect(Collectors.toList());
    }

    private ReactionResponseDto convertReactionToResponseDto(MessageReaction messageReaction) {
        ReactionResponseDto reactionResponseDto = new ReactionResponseDto();
        reactionResponseDto.setId(messageReaction.getId());
        reactionResponseDto.setReactionType(messageReaction.getReactionType());
        reactionResponseDto.setUser(chatRoomMembersService.getUserDtoById(messageReaction.getUser().getId()));
        return reactionResponseDto;
    }

    private AttachmentResponseDto convertAttachmentToResponseDto(Attachment attachment) {
        AttachmentResponseDto attachmentResponseDto = new AttachmentResponseDto();
        attachmentResponseDto.setId(attachment.getId());
        attachmentResponseDto.setMessageId(attachment.getMessage().getId());
        attachmentResponseDto.setFileName(attachment.getFileName());
        attachmentResponseDto.setFilePath(attachment.getFilePath());
        attachmentResponseDto.setFileType(attachment.getFileType());
        return attachmentResponseDto;
    }
}