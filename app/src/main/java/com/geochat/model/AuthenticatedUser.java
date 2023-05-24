package com.geochat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticatedUser {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("UserName")
    private String username;
    @JsonProperty("Email")
    private String email;

    public AuthenticatedUser(){
    }

    public AuthenticatedUser(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
