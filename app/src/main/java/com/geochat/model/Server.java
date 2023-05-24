package com.geochat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Server {
    @JsonProperty("id")
    private final int id;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("url")
    private final String url;

    public Server(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
