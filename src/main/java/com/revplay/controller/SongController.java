package com.revplay.controller;

import com.revplay.dto.SongUpdateRequest;
import com.revplay.entity.Song;
import com.revplay.repository.SongRepository;
import com.revplay.service.SongService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongRepository songRepository;
    private final SongService songService;

    // ✅ GET /songs?page=0&size=10&sort=id,desc&search=ghar
    // ✅ Filters: /songs?genre=Melody OR /songs?artistId=1 OR /songs?albumId=1 OR /songs?releaseYear=2026
    // NOTE: for now, if any filter is present, we return filtered list (no pagination). Otherwise paginated.
    @GetMapping
    public ResponseEntity<?> listSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort,
            @RequestParam(required = false) String search,

            // ✅ filters (optional)
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Long artistId,
            @RequestParam(required = false) Long albumId,
            @RequestParam(required = false) Integer releaseYear
    ) {

        // If any filter is present, use service filter logic (simple version)
        if ((genre != null && !genre.isBlank()) || artistId != null || albumId != null || releaseYear != null) {
            return ResponseEntity.ok(songService.filterSongs(genre, artistId, albumId, releaseYear));
        }

        // Otherwise, normal pagination + sorting + search
        String[] s = sort.split(",");
        Sort.Direction direction = (s.length > 1) ? Sort.Direction.fromString(s[1]) : Sort.Direction.DESC;
        String sortField = s[0];

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

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