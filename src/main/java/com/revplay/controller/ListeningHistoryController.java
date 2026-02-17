package com.revplay.controller;

import com.revplay.service.ListeningHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class ListeningHistoryController {

    private final ListeningHistoryService historyService;

    // Call this when song starts playing
    @PostMapping("/play/{songId}")
    public ResponseEntity<?> track(@PathVariable Long songId) {
        historyService.trackPlay(songId);
        return ResponseEntity.ok("Play tracked");
    }

    // Last 50
    @GetMapping("/recent")
    public ResponseEntity<?> recent() {
        return ResponseEntity.ok(historyService.recent());
    }

    // Full history (pagination)
    @GetMapping
    public ResponseEntity<?> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(historyService.all(page, size));
    }

    // Clear history
    @DeleteMapping
    public ResponseEntity<?> clear() {
        historyService.clear();
        return ResponseEntity.ok("History cleared");
    }
}
