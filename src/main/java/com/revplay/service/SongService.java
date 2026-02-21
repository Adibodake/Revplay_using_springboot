package com.revplay.service;

import com.revplay.dto.SongUpdateRequest;
import com.revplay.dto.SongUpdateResponse;
import com.revplay.dto.TrendingSongResponse;
import com.revplay.entity.Album;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.entity.enums.SongVisibility;
import com.revplay.repository.AlbumRepository;
import com.revplay.repository.ArtistProfileRepository;
import com.revplay.repository.ListeningHistoryRepository;
import com.revplay.repository.SongRepository;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;
import com.revplay.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final StorageService storageService;
    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;
    private final ListeningHistoryRepository historyRepository;
    private final AlbumRepository albumRepository;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ArtistProfile artistProfile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));

        song.setArtist(artistProfile);

        return songRepository.save(song);
    }

    public List<TrendingSongResponse> trending(int limit) {
        return historyRepository.trendingSongs(PageRequest.of(0, limit))
                .stream()
                .map(r -> new TrendingSongResponse(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        ((Number) r[2]).longValue()
                ))
                .toList();
    }

    private ArtistProfile currentArtist() {
        String username = SecurityUtil.currentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));
    }

    @Transactional
    public SongUpdateResponse updateSongAsArtist(Long songId, SongUpdateRequest req) {
        ArtistProfile artist = currentArtist();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        // ownership check
        if (song.getArtist() == null || !song.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You can update only your own songs");
        }

        song.setTitle(req.getTitle());
        song.setGenre(req.getGenre());

        if (req.getVisibility() != null) {
            song.setVisibility(req.getVisibility());
        }

        // album change (null => remove album)
        if (req.getAlbumId() == null) {
            song.setAlbum(null);
        } else {
            Album album = albumRepository.findById(req.getAlbumId())
                    .orElseThrow(() -> new IllegalArgumentException("Album not found"));

            // album ownership check
            if (!album.getArtist().getId().equals(artist.getId())) {
                throw new IllegalArgumentException("You can assign only your own album");
            }
            song.setAlbum(album);
        }

        Song saved = songRepository.save(song);

        return new SongUpdateResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getGenre(),
                saved.getVisibility(),
                saved.getAlbum() != null ? saved.getAlbum().getId() : null,
                saved.getAudioUrl(),
                saved.getCoverUrl()
        );
    }

    @Transactional
    public void deleteSongAsArtist(Long songId) {
        ArtistProfile artist = currentArtist();

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (song.getArtist() == null || !song.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You can delete only your own songs");
        }

        // Optional: delete files from storage if you have a storage service
        // storageService.delete(song.getAudioUrl());
        // storageService.delete(song.getCoverUrl());

        songRepository.delete(song);
    }
}