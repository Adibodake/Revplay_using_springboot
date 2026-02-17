package com.revplay.dto;

import java.time.LocalDateTime;

public record HistoryResponse(
        Long historyId,
        Long songId,
        String title,
        String audioUrl,
        String coverUrl,
        LocalDateTime playedAt
) {}
