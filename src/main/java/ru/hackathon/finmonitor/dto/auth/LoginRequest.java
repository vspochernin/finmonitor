package ru.hackathon.finmonitor.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
} 