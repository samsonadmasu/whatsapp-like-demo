package com.hyperhire.whatsapp.services;

import com.hyperhire.whatsapp.dto.request.MessageReactionDTO;
import com.hyperhire.whatsapp.dto.response.MessageReactionResponseDto;
import com.hyperhire.whatsapp.entity.Message;
import com.hyperhire.whatsapp.entity.MessageReaction;
import com.hyperhire.whatsapp.entity.User;
import com.hyperhire.whatsapp.repositorys.MessageReactionRepository;
import com.hyperhire.whatsapp.shared.enums.EmojiType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageReactionService {
    private final MessageReactionRepository messageReactionRepository;
    private final MessageService messageService;
    private final UserProfileService userProfileService;
    private final ChatRoomMembersService chatRoomMembersService;
    private final ChatroomService chatRoomService;

    @Autowired
    public MessageReactionService(MessageReactionRepository messageReactionRepository, MessageService messageService, UserProfileService userProfileService, ChatRoomMembersService chatRoomMembersService, ChatroomService chatRoomService) {
        this.messageReactionRepository = messageReactionRepository;
        this.messageService = messageService;
        this.userProfileService = userProfileService;
        this.chatRoomMembersService = chatRoomMembersService;
        this.chatRoomService = chatRoomService;
    }

    public void reactToMessage(Long messageId, String emojiType, Long userId) {
        // Retrieve the message from the database using its ID
        Optional<Message> optionalMessage = Optional.ofNullable(messageService.getMessageById(messageId));

        if (optionalMessage.isPresent()) {
            // If the message exists, create a new message reaction entity
            Message message = optionalMessage.get();
            User user = userProfileService.getUserById(userId); // Implement a method to get the current user

            var messageReaction = new MessageReaction();
            messageReaction.setMessage(message);
            messageReaction.setUser(user);
            messageReaction.setReactionType(EmojiType.valueOf(emojiType));
            messageReaction.setCreatedDate(LocalDateTime.now());
            messageReaction.setUpdatedDate(LocalDateTime.now());

            // Save the message reaction to the database
            messageReactionRepository.save(messageReaction);
        } else {
            // Handle the case where the message does not exist
            throw new RuntimeException("Message with ID " + messageId + " not found");
        }
    }

/*    public MessageReaction getMessageReactionById(Long id) {
        return messageReactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message reaction with ID " + id + " not found"));
    }*/

/*    public List<MessageReaction> getMessageReactionsByMessageId(Long messageId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messageReactionRepository.findByMessageId(messageId, pageable).getContent();
    }*/

    public MessageReactionResponseDto getMessageReactionById(Long id) {
        MessageReaction messageReaction = messageReactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message reaction with ID " + id + " not found"));

        // Convert MessageReaction entity to MessageReactionResponseDto
        MessageReactionResponseDto responseDto = convertToResponseDto(messageReaction);

        return responseDto;
    }

    public List<MessageReactionResponseDto> getMessageReactionsByMessageId(Long messageId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<MessageReaction> messageReactions = messageReactionRepository.findByMessageId(messageId, pageable).getContent();

        // Convert MessageReaction entities to MessageReactionResponseDto objects
        List<MessageReactionResponseDto> responseDtos = new ArrayList<>();
        for (MessageReaction messageReaction : messageReactions) {
            MessageReactionResponseDto responseDto = convertToResponseDto(messageReaction);
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    public void updateMessageReaction(Long reactionId, MessageReactionDTO updatedReaction) {
        // Retrieve the existing message reaction from the database
        MessageReaction existingReaction = messageReactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Message reaction with ID " + reactionId + " not found"));

        // Update the fields with the new values from the DTO
        existingReaction.setReactionType(EmojiType.valueOf(updatedReaction.getReactionType()));

        // Save the updated message reaction
        messageReactionRepository.save(existingReaction);
    }

    @Transactional
    public void deleteMessageReaction(Long reactionId) {
        messageReactionRepository.deleteById(reactionId);
    }

    public MessageReactionResponseDto convertToResponseDto(MessageReaction messageReaction) {
        MessageReactionResponseDto responseDto = new MessageReactionResponseDto();
        responseDto.setId(messageReaction.getId());
        responseDto.setMessage(messageService.convertToResponseDto(messageReaction.getMessage())); // Assuming you have a converter for Message entities to MessageDto
        responseDto.setUser(chatRoomMembersService.getUserDtoById(messageReaction.getUser().getId())); // Assuming you have a converter for User entities to UserDto
        responseDto.setReactionType(messageReaction.getReactionType());
        return responseDto;
    }
}
