package com.revplay.controller;

import com.revplay.dto.ArtistProfileRequest;
import com.revplay.entity.ArtistProfile;
import com.revplay.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    // ✅ any logged-in user can call this once to become ARTIST
    @PostMapping("/become")
    public ResponseEntity<?> becomeArtist() {
        artistService.becomeArtist();
        return ResponseEntity.ok("Role updated to ARTIST");
    }

    // ✅ create/update profile (ARTIST only)
    @PostMapping("/profile")
    public ResponseEntity<?> upsertProfile(@Valid @RequestBody ArtistProfileRequest req) {
        ArtistProfile saved = artistService.upsertProfile(req);
        return ResponseEntity.ok(saved);
    }

    // ✅ view own profile (ARTIST only)
    @GetMapping("/profile/me")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok(artistService.myProfile());
    }
}
