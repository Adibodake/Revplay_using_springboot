package com.revplay.dto;

public record TrendingSongResponse(
        Long songId,
        String title,
        long plays
) {}