package com.revplay.controller;

import com.revplay.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    // Artist follow
    @PostMapping("/artist/{id}")
    public ResponseEntity<?> followArtist(@PathVariable Long id) {
        return ResponseEntity.ok(followService.followArtist(id));
    }

    @DeleteMapping("/artist/{id}")
    public ResponseEntity<?> unfollowArtist(@PathVariable Long id) {
        return ResponseEntity.ok(followService.unfollowArtist(id));
    }

    @GetMapping("/artist/mine")
    public ResponseEntity<?> myArtists() {
        return ResponseEntity.ok(followService.myFollowedArtists());
    }

    // Playlist follow
    @PostMapping("/playlist/{id}")
    public ResponseEntity<?> followPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(followService.followPlaylist(id));
    }

    @DeleteMapping("/playlist/{id}")
    public ResponseEntity<?> unfollowPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(followService.unfollowPlaylist(id));
    }

    @GetMapping("/playlist/mine")
    public ResponseEntity<?> myPlaylists() {
        return ResponseEntity.ok(followService.myFollowedPlaylists());
    }
}