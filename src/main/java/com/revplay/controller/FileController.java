package com.revplay.controller;

import com.revplay.entity.Song;
import com.revplay.entity.enums.SongVisibility;
import com.revplay.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    private final Path audioDir;
    private final Path imageDir;

    public FileController(
            @Value("${app.storage.audio-dir}") String audioDir,
            @Value("${app.storage.image-dir}") String imageDir
    ) {
        this.audioDir = Paths.get(audioDir).toAbsolutePath().normalize();
        this.imageDir = Paths.get(imageDir).toAbsolutePath().normalize();
    }

    @GetMapping("/audio/{filename:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String filename) throws MalformedURLException {
        return serveFile(audioDir.resolve(filename), MediaType.APPLICATION_OCTET_STREAM);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        return serveFile(imageDir.resolve(filename), MediaType.IMAGE_JPEG);
    }

    private ResponseEntity<Resource> serveFile(Path filePath, MediaType fallbackType) throws MalformedURLException {
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(fallbackType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                .body(resource);
    }

    @RestController
    @RequestMapping("/artist/songs")
    @RequiredArgsConstructor
    public static class ArtistSongController {

        private final SongService songService;

        @PostMapping(consumes = {"multipart/form-data"})
        public ResponseEntity<?> upload(
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

            return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
        }
    }
}
