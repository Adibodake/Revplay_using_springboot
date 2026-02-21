package com.revplay.controller;

import com.revplay.entity.Song;
import com.revplay.repository.SongRepository;
import com.revplay.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revplay.dto.SongUpdateRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongRepository songRepository;
    private final SongService songService;

    // GET /songs?page=0&size=10&sort=id,desc&search=ghar
    @GetMapping
    public ResponseEntity<?> listSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @RequestParam(required = false) String search
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));

        Page<Song> result = (search == null || search.isBlank())
                ? songRepository.findAll(pageable)
                : songRepository.findByTitleContainingIgnoreCase(search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSong(@PathVariable Long id) {
        return songRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/trending")
    public ResponseEntity<?> trending(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(songService.trending(limit));
    }

    // ✅ Update song (ARTIST only)
    @PutMapping("/artist/{songId}")
    public ResponseEntity<?> updateSong(
            @PathVariable Long songId,
            @Valid @RequestBody SongUpdateRequest request
    ) {
        return ResponseEntity.ok(songService.updateSongAsArtist(songId, request));
    }

    // ✅ Delete song (ARTIST only)
    @DeleteMapping("/artist/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable Long songId) {
        songService.deleteSongAsArtist(songId);
        return ResponseEntity.ok("Song deleted successfully");
    }
}
