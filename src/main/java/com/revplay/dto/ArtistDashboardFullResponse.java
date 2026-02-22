package com.revplay.dto;

import java.util.List;

public record ArtistDashboardFullResponse(
        long totalSongs,
        long totalPlays,
        long totalFavorites,
        long followerCount,
        List<ArtistDashboardResponse.TopSong> topSongs,
        List<TopListenerResponse> topListeners,
        List<TrendPointResponse> dailyTrends,
        List<TrendPointResponse> weeklyTrends,
        List<TrendPointResponse> monthlyTrends
) {}