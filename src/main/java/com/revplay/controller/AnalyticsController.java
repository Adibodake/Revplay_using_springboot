package com.revplay.controller;

import com.revplay.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
