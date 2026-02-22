package com.revplay.controller;

import com.revplay.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revplay.entity.enums.TrendBucket;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/me/stats")
    public ResponseEntity<?> myStats() {
        return ResponseEntity.ok(analyticsService.myStats());
    }

    @GetMapping("/artist/dashboard")
    public ResponseEntity<?> artistDashboard(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.artistDashboard(limit));
    }

    // ✅ NEW: Top listeners for artist songs
    @GetMapping("/artist/top-listeners")
    public ResponseEntity<?> topListeners(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.topListeners(limit));
    }

    @GetMapping("/artist/favoriters")
    public ResponseEntity<?> favoriters(@RequestParam(required = false) Long songId) {
        return ResponseEntity.ok(analyticsService.favoriters(songId));
    }

    @GetMapping("/artist/trends")
    public ResponseEntity<?> trends(
            @RequestParam(defaultValue = "DAILY") TrendBucket bucket,
            @RequestParam(defaultValue = "30") int days
    ) {
        return ResponseEntity.ok(analyticsService.trends(bucket, days));
    }

    // ==========================================================
    // ✅ NEW: FULL DASHBOARD (one API = stats + songs + listeners + trends)
    // URL: /analytics/artist/full-dashboard?topSongsLimit=10&topListenersLimit=5&days=30
    // ==========================================================
    @GetMapping("/artist/full-dashboard")
    public ResponseEntity<?> fullDashboard(
            @RequestParam(defaultValue = "10") int topSongsLimit,
            @RequestParam(defaultValue = "5") int topListenersLimit,
            @RequestParam(defaultValue = "30") int days
    ) {
        return ResponseEntity.ok(
                analyticsService.fullDashboard(topSongsLimit, topListenersLimit, days)
        );
    }
}