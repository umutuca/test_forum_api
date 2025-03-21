package com.example.forum_api.DTO;

public class MessageDTO {

    private Long id;
    private String content;
    private Long channelId;
    private String channelName;
    private Long createdById;
    private String createdByUsername;
    private String timestamp;

    public MessageDTO(Long id, String content, Long channelId, String channelName,
                      Long createdById, String createdByUsername, String timestamp) {
        this.id = id;
        this.content = content;
        this.channelId = channelId;
        this.channelName = channelName;
        this.createdById = createdById;
        this.createdByUsername = createdByUsername;
        this.timestamp = timestamp;
    }

    // Getters och Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
