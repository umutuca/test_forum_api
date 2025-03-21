package com.example.forum_api.Controller;

import com.example.forum_api.DTO.MessageDTO;
import com.example.forum_api.models.Message;
import com.example.forum_api.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        List<MessageDTO> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChannel(@PathVariable Long channelId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByChannel(channelId);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{channelId}")
    public ResponseEntity<MessageDTO> createMessage(@PathVariable Long channelId, @RequestBody Message message) {
        try {
            MessageDTO createdMessage = messageService.createMessage(channelId, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<MessageDTO> updateMessageContent(@PathVariable Long messageId, @RequestBody Map<String, String> requestBody) {
        try {
            // Logga hela requestBody för att verifiera inkommande data
            System.out.println("Request Body: " + requestBody);

            String newContent = requestBody.get("content");

            if (newContent == null || newContent.trim().isEmpty()) {
                System.out.println("Invalid content: " + newContent);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            MessageDTO updatedMessage = messageService.updateMessage(messageId, newContent);
            return ResponseEntity.ok(updatedMessage);
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
