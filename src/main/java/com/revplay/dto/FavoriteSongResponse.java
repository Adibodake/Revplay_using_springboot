package com.revplay.dto;

import java.time.LocalDateTime;

public record FavoriteSongResponse(
        Long songId,
        String title,
        String artistName,
        int durationSec,
        String audioUrl,
        String coverUrl,
        LocalDateTime favoritedAt
) {}