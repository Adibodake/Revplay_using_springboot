package com.revplay.dto;

import java.time.LocalDateTime;

public record HistoryItemResponse(
        Long id,
        Long songId,
        String title,
        String artistName,
        int durationSec,
        String coverUrl,
        LocalDateTime playedAt,
        int listenedSeconds
) {}