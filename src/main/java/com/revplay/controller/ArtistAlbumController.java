package com.revplay.controller;

import com.revplay.dto.AlbumRequest;
import com.revplay.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artist/albums")
@RequiredArgsConstructor
public class ArtistAlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AlbumRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(req));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> mine() {
        return ResponseEntity.ok(albumService.myAlbums());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AlbumRequest req) {
        return ResponseEntity.ok(albumService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<?> addSong(@PathVariable Long albumId, @PathVariable Long songId) {
        return ResponseEntity.ok(albumService.addSong(albumId, songId));
    }

    @DeleteMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<?> removeSong(@PathVariable Long albumId, @PathVariable Long songId) {
        return ResponseEntity.ok(albumService.removeSong(albumId, songId));
    }
}
