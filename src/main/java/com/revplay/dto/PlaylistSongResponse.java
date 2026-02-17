package com.revplay.dto;

public record PlaylistSongResponse(
        Long songId,
        String title,
        String audioUrl,
        String coverUrl,
        int position
) {}
