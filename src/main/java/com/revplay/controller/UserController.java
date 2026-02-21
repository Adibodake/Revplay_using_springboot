package com.revplay.controller;

import com.revplay.dto.UpdateProfileRequest;
import com.revplay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ View my profile
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(userService.me());
    }

    // ✅ Update display name + bio
    @PutMapping("/me")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(userService.updateProfile(req));
    }

    // ✅ Upload profile picture
    @PostMapping(value = "/me/profile-picture", consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(@RequestPart MultipartFile file) {
        return ResponseEntity.ok(userService.uploadProfilePicture(file));
    }
}