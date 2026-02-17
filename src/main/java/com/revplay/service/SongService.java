package com.revplay.service;

import com.revplay.entity.Song;
import com.revplay.entity.enums.SongVisibility;
import com.revplay.repository.SongRepository;
import com.revplay.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.revplay.entity.ArtistProfile;
import com.revplay.repository.ArtistProfileRepository;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;


@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final StorageService storageService;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;


    public Song uploadSong(
            String title,
            String genre,
            int durationSec,
            SongVisibility visibility,
            MultipartFile audioFile,
            MultipartFile coverImage
    ) {

        String audioUrl = storageService.saveAudio(audioFile);

        if (audioUrl == null) {
            throw new IllegalArgumentException("Audio file is required");
        }

        String coverUrl = storageService.saveImage(coverImage);

        Song song = Song.builder()
                .title(title)
                .genre(genre)
                .durationSec(durationSec)
                .visibility(visibility)
                .audioUrl(audioUrl)
                .coverUrl(coverUrl)
                .build();

        // ✅ attach logged-in artist profile
        String username = SecurityUtil.currentUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));

        song.setArtist(artistProfile);


        return songRepository.save(song);
    }
}
