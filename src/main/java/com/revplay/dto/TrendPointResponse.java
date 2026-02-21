package com.revplay.dto;

public record TrendPointResponse(
        String bucketKey, // e.g. "2026-02-16" or "2026-W07" or "2026-02"
        long plays
) {}