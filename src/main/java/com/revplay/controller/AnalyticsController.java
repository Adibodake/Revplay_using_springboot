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
}
