package com.revplay.dto;

import java.time.LocalDate;
import java.util.List;

public record AlbumResponse(
        Long id,
        String name,
        String description,
        LocalDate releaseDate,
        String coverUrl,
        Long artistProfileId,
        String artistName,
        List<TrackResponse> tracks
) {
    public record TrackResponse(Long id, String title, int durationSec, String audioUrl, String coverUrl) {}
}
