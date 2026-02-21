package com.revplay.dto;

public record TopListenerResponse(
        Long userId,
        String username,
        long plays
) {}