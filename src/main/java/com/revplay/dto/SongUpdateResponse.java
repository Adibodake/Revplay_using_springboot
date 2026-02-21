package com.revplay.dto;

import com.revplay.entity.enums.SongVisibility;

public record SongUpdateResponse(
        Long id,
        String title,
        String genre,
        SongVisibility visibility,
        Long albumId,
        String audioUrl,
        String coverUrl
) {}