package com.geochat.model.read_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserReadDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("userName")
    private String userName;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username != null ? username : userName;
    }
}
