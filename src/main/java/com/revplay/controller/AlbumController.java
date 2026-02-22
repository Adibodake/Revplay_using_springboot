package com.revplay.controller;

import com.revplay.dto.AlbumRequest;
import com.revplay.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    // ✅ PUBLIC: list all albums
    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(albumService.allAlbums());
    }

    // ✅ PUBLIC: album details
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.get(id));
    }

    // ✅ ARTIST: create album
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AlbumRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(req));
    }

    // ✅ ARTIST: list my albums
    @GetMapping("/me")
    public ResponseEntity<?> myAlbums() {
        return ResponseEntity.ok(albumService.myAlbums());
    }

    // ✅ ARTIST: update album
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AlbumRequest req) {
        return ResponseEntity.ok(albumService.update(id, req));
    }

    // ✅ ARTIST: delete album (only if no songs)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.ok("Album deleted successfully");
    }

    // ✅ ARTIST: add song to album
    @PostMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<?> addSong(@PathVariable Long albumId, @PathVariable Long songId) {
        return ResponseEntity.ok(albumService.addSong(albumId, songId));
    }

    // ✅ ARTIST: remove song from album
    @DeleteMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<?> removeSong(@PathVariable Long albumId, @PathVariable Long songId) {
        return ResponseEntity.ok(albumService.removeSong(albumId, songId));
    }
}