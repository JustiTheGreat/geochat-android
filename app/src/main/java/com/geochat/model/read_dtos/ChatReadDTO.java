package com.geochat.model.read_dtos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geochat.model.Message;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ChatReadDTO {
    @JsonProperty("id")
    private int id;
    @JsonProperty("messages")
    private List<Message> messages;
    @JsonProperty("chatMembers")
    private List<UserReadDTO> chatMembers;
    @JsonProperty("chatName")
    private String chatName;
    @JsonProperty("locationId")
    private Integer locationId;

    public int getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<UserReadDTO> getChatMembers() {
        return chatMembers;
    }

    public String getChatName() {
        return chatName;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public String getLastMessageText() {
        if (messages.isEmpty()) return "";
        Optional<Message> message = messages.stream().max(Comparator.comparing(Message::getTimeSent));
        if (message.isEmpty()) return "";
        return "Last message: " + message.get().getContent();
    }
}
