package com.geochat.model.read_dtos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geochat.model.MessageReadDto;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ChatReadDto {
    @JsonProperty("id")
    public int id;
    @JsonProperty("messages")
    public List<MessageReadDto> messages;
    @JsonProperty("chatMembers")
    public List<UserReadDTO> chatMembers;
    @JsonProperty("chatName")
    public String chatName;
    @JsonProperty("locationId")
    public Integer locationId;

    public int getId() {
        return id;
    }

    public List<MessageReadDto> getMessages() {
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

    public void addMessage(MessageReadDto message) {
        messages.add(0, message);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public String getLastMessageText() {
        if (messages.isEmpty()) return "";
        Optional<MessageReadDto> message = messages.stream().max(Comparator.comparing(MessageReadDto::getTimeSent));
        if (message.isEmpty()) return "";
        return "Last message: " + message.get().getContent();
    }
}
