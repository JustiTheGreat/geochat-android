package com.geochat.model;

import com.geochat.model.read_dtos.UserReadDTO;

public class SearchItem {
    private final UserReadDTO user;
    private final String country;

    public SearchItem(UserReadDTO user, String country) {
        this.user = user;
        this.country = country;
    }

    public String getUserId() {
        return user.getId();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getCountry() {
        return country;
    }
}
