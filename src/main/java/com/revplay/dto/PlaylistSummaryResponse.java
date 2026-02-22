package com.revplay.dto;

import com.revplay.entity.enums.PlaylistPrivacy;

import java.time.LocalDateTime;

public record PlaylistSummaryResponse(
        Long id,
        String name,
        String description,
        PlaylistPrivacy privacy,
        long totalSongs,
        LocalDateTime createdAt
) {}