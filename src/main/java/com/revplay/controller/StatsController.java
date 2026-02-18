package com.revplay.controller;

import com.revplay.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/me")
    public ResponseEntity<?> myStats() {
        return ResponseEntity.ok(statsService.myStats());
    }

    @GetMapping("/artist")
    public ResponseEntity<?> artistDashboard(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.artistDashboard(limit));
    }
}
