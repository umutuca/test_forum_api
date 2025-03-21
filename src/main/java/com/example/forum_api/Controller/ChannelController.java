package com.example.forum_api.Controller;

import com.example.forum_api.DTO.ChannelDTO;
import com.example.forum_api.service.ChannelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    // Hämta alla kanaler
    @GetMapping("/")
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        List<ChannelDTO> channels = channelService.getAllChannels();
        if (channels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(channels);
    }

    // Hämta meddelanden i en specifik kanal
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMessagesInChannel(@PathVariable Long id) {
        return channelService.getMessagesInChannel(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", String.format("Channel with ID %d not found.", id));
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    // Skapa en ny kanal
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createChannel(@RequestBody Map<String, String> requestBody) {
        try {
            ChannelDTO newChannel = channelService.createChannel(requestBody);
            Map<String, Object> response = new HashMap<>();
            response.put("id", newChannel.getId());
            response.put("name", newChannel.getName());
            response.put("createdBy", newChannel.getCreatedBy());
            response.put("createdAt", newChannel.getCreatedAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // Lägg till ett meddelande i en kanal
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> addMessageToChannel(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        return channelService.addMessageToChannel(id, requestBody)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Failed to add message to the channel.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                });
    }

    // Ta bort en kanal
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteChannel(@PathVariable Long id) {
        try {
            channelService.deleteChannel(id); // Service-metod för att ta bort kanalen
            return ResponseEntity.ok("Channel with ID " + id + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
