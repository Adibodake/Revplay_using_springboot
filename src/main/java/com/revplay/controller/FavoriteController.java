package com.revplay.controller;

import com.revplay.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{songId}")
    public ResponseEntity<?> add(@PathVariable Long songId) {
        favoriteService.addFavorite(songId);
        return ResponseEntity.ok("Added to favorites");
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<?> remove(@PathVariable Long songId) {
        favoriteService.removeFavorite(songId);
        return ResponseEntity.ok("Removed from favorites");
    }

    // ✅ changed from @GetMapping to /me (best practice)
    @GetMapping("/me")
    public ResponseEntity<?> myFavorites() {
        return ResponseEntity.ok(favoriteService.myFavorites());
    }
}