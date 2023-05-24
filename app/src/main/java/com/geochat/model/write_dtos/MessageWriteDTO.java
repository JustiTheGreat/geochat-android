package com.geochat.model.write_dtos;

public class MessageWriteDTO {
    private final int ChatId;
    private final String UserId;
    private final String Content;

    public MessageWriteDTO(int ChatId, String UserId, String Content) {
        this.ChatId = ChatId;
        this.UserId = UserId;
        this.Content = Content;
    }

    public int getChatId() {
        return ChatId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getContent() {
        return Content;
    }
}
