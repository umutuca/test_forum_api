package com.example.forum_api.service;

import com.example.forum_api.DTO.MessageDTO;
import com.example.forum_api.models.Channel;
import com.example.forum_api.models.Message;
import com.example.forum_api.Repository.ChannelRepository;
import com.example.forum_api.Repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    public MessageService(MessageRepository messageRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }

    // Hämta alla meddelanden
    public List<MessageDTO> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    // Hämta alla meddelanden i en specifik kanal
    public List<MessageDTO> getMessagesByChannel(Long channelId) {
        verifyChannelExists(channelId);
        return messageRepository.findByChannelId(channelId).stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    // Skapa ett nytt meddelande i en specifik kanal
    public MessageDTO createMessage(Long channelId, Message message) {
        Channel channel = verifyChannelExists(channelId);
        message.setChannel(channel);
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        return toMessageDTO(savedMessage);
    }

    // Uppdatera innehållet i ett meddelande
    public MessageDTO updateMessage(Long messageId, String newContent) {
        // Kontrollera om meddelandet finns
        Message message = verifyMessageExists(messageId);

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Field 'content' is required and cannot be empty.");
        }

        // Uppdatera innehåll
        message.setContent(newContent);
        Message updatedMessage = messageRepository.save(message);

        return toMessageDTO(updatedMessage);
    }


    // Ta bort ett meddelande
    public void deleteMessage(Long messageId) {
        verifyMessageExists(messageId);
        messageRepository.deleteById(messageId);
    }

    // Hämta meddelanden skapade efter ett visst datum
    public List<MessageDTO> getMessagesAfter(LocalDateTime timestamp) {
        return messageRepository.findByTimestampAfter(timestamp).stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    // Sök meddelanden som innehåller ett visst nyckelord
    public List<MessageDTO> searchMessagesByKeyword(String keyword) {
        return messageRepository.searchByContentContaining(keyword).stream()
                .map(this::toMessageDTO)
                .collect(Collectors.toList());
    }

    // Hjälpmetod för att verifiera om en kanal existerar
    private Channel verifyChannelExists(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel with ID " + channelId + " not found."));
    }

    // Hjälpmetod för att verifiera om ett meddelande existerar
    private Message verifyMessageExists(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message with ID " + messageId + " not found."));
    }

    // Hjälpmetod för att konvertera `Message` till `MessageDTO`
    private MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getChannel().getId(),
                message.getChannel().getName(),
                message.getCreatedBy().getId(),
                message.getCreatedBy().getUsername(),
                message.getTimestamp().toString()
        );
    }
}
