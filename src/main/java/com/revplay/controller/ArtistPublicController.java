package com.revplay.controller;

import com.revplay.service.ArtistPublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistPublicController {

    private final ArtistPublicService artistPublicService;

    @GetMapping("/{artistId}")
    public ResponseEntity<?> getArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistPublicService.getArtist(artistId));
    }

    @GetMapping("/{artistId}/songs")
    public ResponseEntity<?> artistSongs(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistPublicService.artistSongs(artistId));
    }

    @GetMapping("/{artistId}/albums")
    public ResponseEntity<?> artistAlbums(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistPublicService.artistAlbums(artistId));
    }
}