package com.geochat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageReadDto {
    @JsonProperty("id")
    public int id;
    @JsonProperty("chatId")
    public int chatId;
    @JsonProperty("userId")
    public String userId;
    @JsonProperty("userName")
    public String userName;
    @JsonProperty("content")
    public String content;
    @JsonProperty("timeSent")
    public String timeSent;

    public int getId() {
        return id;
    }

    public int getChatId() {
        return chatId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public String getTimeSent() {
        return timeSent;
    }
}
