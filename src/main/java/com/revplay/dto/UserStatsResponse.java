package com.revplay.dto;

public record UserStatsResponse(
        long totalPlaylists,
        long favoriteSongs,
        long totalPlays,
        long listeningSeconds
) {}
