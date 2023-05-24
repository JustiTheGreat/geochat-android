package com.geochat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty("id")
    private int id;
    @JsonProperty("chatId")
    private int chatId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("userName")
    private String username;
    @JsonProperty("content")
    private String content;
    @JsonProperty("timeSent")
    private String timeSent;

    public int getId() {
        return id;
    }

    public int getChatId() {
        return chatId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTimeSent() {
        return timeSent;
    }
}
