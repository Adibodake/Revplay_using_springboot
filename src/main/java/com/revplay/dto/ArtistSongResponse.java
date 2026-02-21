package com.revplay.dto;

import com.revplay.entity.enums.SongVisibility;
import java.time.LocalDate;

public record ArtistSongResponse(
        Long id,
        String title,
        String genre,
        int durationSec,
        SongVisibility visibility,
        LocalDate releaseDate,
        String audioUrl,
        String coverUrl,
        Long albumId
) {}