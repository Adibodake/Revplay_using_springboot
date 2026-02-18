package com.revplay.dto;

import java.util.List;

public record ArtistDashboardResponse(
        long totalSongs,
        long totalPlays,
        long totalFavorites,
        List<TopSong> topSongs
) {
    public record TopSong(Long songId, String title, long plays) {}
}
