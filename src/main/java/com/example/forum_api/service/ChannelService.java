package com.example.forum_api.service;

import com.example.forum_api.DTO.ChannelDTO;
import com.example.forum_api.models.Channel;
import com.example.forum_api.models.Message;
import com.example.forum_api.models.User;
import com.example.forum_api.Repository.ChannelRepository;
import com.example.forum_api.Repository.MessageRepository;
import com.example.forum_api.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // Hämta alla kanaler
    public List<ChannelDTO> getAllChannels() {
        return channelRepository.findAll().stream()
                .map(channel -> new ChannelDTO(
                        channel.getId(),
                        channel.getName(),
                        channel.getCreatedBy().getUsername(),
                        channel.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    // Hämta meddelanden i en specifik kanal
    public Optional<Map<String, Object>> getMessagesInChannel(Long id) {
        Optional<Channel> channelOpt = channelRepository.findById(id);
        if (channelOpt.isEmpty()) {
            return Optional.empty();
        }

        Channel channel = channelOpt.get();
        List<Message> messages = messageRepository.findByChannelId(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("channel", channel.getName());
        response.put("messages", messages.stream().map(message -> {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", message.getId());
            messageData.put("content", message.getContent());
            messageData.put("timestamp", message.getTimestamp());
            messageData.put("createdBy", message.getCreatedBy().getUsername());
            return messageData;
        }).collect(Collectors.toList()));

        return Optional.of(response);
    }

    // Skapa en ny kanal
    public ChannelDTO createChannel(Map<String, String> requestBody) {
        String channelName = requestBody.get("name");
        String description = requestBody.get("description"); // Beskrivning är valfritt
        String username = requestBody.get("username");

        if (channelName == null || username == null) {
            return null;
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        Channel channel = new Channel(channelName, description, user); // Använd beskrivning
        Channel savedChannel = channelRepository.save(channel);

        return new ChannelDTO(
                savedChannel.getId(),
                savedChannel.getName(),
                savedChannel.getCreatedBy().getUsername(),
                savedChannel.getCreatedAt().toString()
        );
    }

    // Lägg till ett meddelande i en kanal
    public Optional<Map<String, Object>> addMessageToChannel(Long id, Map<String, String> requestBody) {
        String content = requestBody.get("content");
        String username = requestBody.get("username");

        if (content == null || username == null) {
            return Optional.empty();
        }

        Optional<Channel> channelOpt = channelRepository.findById(id);
        if (channelOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        Channel channel = channelOpt.get();
        User user = userOpt.get();
        Message message = new Message(content, channel, user);
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedMessage.getId());
        response.put("content", savedMessage.getContent());
        response.put("channel", savedMessage.getChannel().getName());
        response.put("createdBy", savedMessage.getCreatedBy().getUsername());
        response.put("timestamp", savedMessage.getTimestamp());

        return Optional.of(response);
    }

    // Ta bort en kanal
    public void deleteChannel(Long id) {
        if (!channelRepository.existsById(id)) {
            throw new IllegalArgumentException("Channel with ID " + id + " not found.");
        }
        channelRepository.deleteById(id);
    }
}
