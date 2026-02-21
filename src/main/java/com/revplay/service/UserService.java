package com.revplay.service;

import com.revplay.dto.UpdateProfileRequest;
import com.revplay.dto.UserProfileResponse;
import com.revplay.entity.User;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;
import com.revplay.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserProfileResponse me() {
        User u = currentUser();
        return new UserProfileResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getDisplayName(),
                u.getBio(),
                u.getProfilePicUrl(),
                u.getRole().name()
        );
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest req) {
        User u = currentUser();

        u.setDisplayName(req.getDisplayName());
        u.setBio(req.getBio());

        User saved = userRepository.save(u);
        return new UserProfileResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getDisplayName(),
                saved.getBio(),
                saved.getProfilePicUrl(),
                saved.getRole().name()
        );
    }

    @Transactional
    public UserProfileResponse uploadProfilePicture(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Profile picture is required");
        }

        User u = currentUser();

        String url = storageService.saveImage(file);
        u.setProfilePicUrl(url);

        User saved = userRepository.save(u);
        return new UserProfileResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getDisplayName(),
                saved.getBio(),
                saved.getProfilePicUrl(),
                saved.getRole().name()
        );
    }
}