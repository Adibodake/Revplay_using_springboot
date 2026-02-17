package com.revplay.service;

import com.revplay.dto.ArtistProfileRequest;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.User;
import com.revplay.entity.enums.Role;
import com.revplay.repository.ArtistProfileRepository;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // 1) become artist (role change)
    @Transactional
    public void becomeArtist() {
        User user = currentUser();
        if (user.getRole() == Role.ARTIST) return;

        user.setRole(Role.ARTIST);
        userRepository.save(user);
    }

    // 2) create or update artist profile
    @Transactional
    public ArtistProfile upsertProfile(ArtistProfileRequest req) {
        User user = currentUser();

        ArtistProfile profile = artistProfileRepository.findByUser(user)
                .orElse(ArtistProfile.builder().user(user).build());

        profile.setArtistName(req.getArtistName());
        profile.setBio(req.getBio());
        profile.setGenre(req.getGenre());
        profile.setProfilePicUrl(req.getProfilePicUrl());
        profile.setBannerUrl(req.getBannerUrl());
        profile.setInstagramUrl(req.getInstagramUrl());
        profile.setTwitterUrl(req.getTwitterUrl());
        profile.setYoutubeUrl(req.getYoutubeUrl());
        profile.setSpotifyUrl(req.getSpotifyUrl());
        profile.setWebsiteUrl(req.getWebsiteUrl());

        return artistProfileRepository.save(profile);
    }

    public ArtistProfile myProfile() {
        User user = currentUser();
        return artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));
    }
}
