package com.revplay.dto;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String bio,
        String profilePictureUrl,
        String role
) {}