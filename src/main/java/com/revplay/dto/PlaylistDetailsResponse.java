package com.revplay.dto;

import com.revplay.entity.enums.PlaylistPrivacy;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistDetailsResponse(
        Long id,
        String name,
        String description,
        PlaylistPrivacy privacy,
        String ownerUsername,
        long totalSongs,
        LocalDateTime createdAt,
        List<PlaylistSongItem> songs
) {
    public record PlaylistSongItem(
            Long songId,
            String title,
            String artistName,
            int durationSec,
            String coverUrl,
            int position
    ) {}
}