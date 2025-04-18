package ru.hackathon.finmonitor.model;

import java.util.Collections;
import java.util.List;

public enum UserRole {
    ROLE_USER,
    ROLE_ADMIN;

    public List<UserRole> getAuthorities() {
        return Collections.singletonList(this);
    }
} 