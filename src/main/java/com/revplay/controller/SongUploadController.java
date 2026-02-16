package com.revplay.controller;

import com.revplay.entity.Song;
import com.revplay.entity.enums.SongVisibility;
import com.revplay.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongUploadController {

    private final SongService songService;

    // For now: any authenticated user can upload (later we restrict to ARTIST)
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadSong(
            @RequestParam String title,
            @RequestParam(required = false) String genre,
            @RequestParam int durationSec,
            @RequestParam(defaultValue = "PUBLIC") SongVisibility visibility,
            @RequestPart MultipartFile audioFile,
            @RequestPart(required = false) MultipartFile coverImage
    ) {

        Song saved = songService.uploadSong(
                title, genre, durationSec, visibility, audioFile, coverImage
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new SongUploadResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getAudioUrl(),
                saved.getCoverUrl()
        ));
    }

    public record SongUploadResponse(Long id, String title, String audioUrl, String coverUrl) {}
}
