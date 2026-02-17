package com.revplay.controller;

import com.revplay.dto.*;
import com.revplay.service.PlaylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PlaylistCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.create(req));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> mine() {
        return ResponseEntity.ok(playlistService.myPlaylists());
    }

    @GetMapping("/public")
    public ResponseEntity<?> publicPlaylists() {
        return ResponseEntity.ok(playlistService.publicPlaylists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(playlistService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PlaylistUpdateRequest req) {
        return ResponseEntity.ok(playlistService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        playlistService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> addSong(@PathVariable Long id, @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.addSong(id, songId));
    }

    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<?> removeSong(@PathVariable Long id, @PathVariable Long songId) {
        return ResponseEntity.ok(playlistService.removeSong(id, songId));
    }

    @PutMapping("/{id}/songs/reorder")
    public ResponseEntity<?> reorder(@PathVariable Long id, @Valid @RequestBody ReorderRequest req) {
        return ResponseEntity.ok(playlistService.reorder(id, req));
    }
}
