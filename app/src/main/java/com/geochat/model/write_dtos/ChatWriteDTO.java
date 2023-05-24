package com.geochat.model.write_dtos;

public class ChatWriteDTO {
    private final String userId;
    private final String friendUserId;

    public ChatWriteDTO(String userId, String friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }
}
