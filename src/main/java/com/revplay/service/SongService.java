package com.revplay.service;

import com.revplay.entity.Song;
import com.revplay.entity.enums.SongVisibility;
import com.revplay.repository.SongRepository;
import com.revplay.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final StorageService storageService;

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

        return songRepository.save(song);
    }
}
