package com.gamenest.dto.auth;

import com.gamenest.dto.user.UserRequest;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private UserRequest user;
    private String tokenType = "Bearer ";

    public AuthResponse(String accessToken, UserRequest user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
