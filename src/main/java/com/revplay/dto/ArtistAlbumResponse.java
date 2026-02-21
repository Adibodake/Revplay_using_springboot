package com.revplay.dto;

import java.time.LocalDate;

public record ArtistAlbumResponse(
        Long id,
        String name,
        String description,
        LocalDate releaseDate,
        String coverUrl
) {}