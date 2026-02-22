package com.revplay.service;

import com.revplay.dto.*;
import com.revplay.entity.*;
import com.revplay.entity.enums.PlaylistPrivacy;
import com.revplay.repository.*;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ✅ helper to fetch playlist songs in order
    private List<PlaylistSong> playlistSongs(Playlist p) {
        return playlistSongRepository.findByPlaylistOrderByPositionAsc(p);
    }

    @Transactional
    public PlaylistResponse create(PlaylistCreateRequest req) {
        User owner = currentUser();

        Playlist playlist = Playlist.builder()
                .owner(owner)
                .name(req.getName())
                .description(req.getDescription())
                .privacy(req.getPrivacy() == null ? PlaylistPrivacy.PRIVATE : req.getPrivacy())
                .build();

        Playlist saved = playlistRepository.save(playlist);
        return toResponse(saved);
    }

    public List<PlaylistResponse> myPlaylists() {
        User owner = currentUser();
        return playlistRepository.findByOwnerOrderByCreatedAtDesc(owner)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PlaylistResponse> publicPlaylists() {
        return playlistRepository.findByPrivacyOrderByCreatedAtDesc(PlaylistPrivacy.PUBLIC)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlaylistResponse getById(Long playlistId) {
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        // If private, only owner can view
        if (p.getPrivacy() == PlaylistPrivacy.PRIVATE) {
            User me = currentUser();
            if (!p.getOwner().getId().equals(me.getId())) {
                throw new IllegalArgumentException("This playlist is private");
            }
        }

        return toResponse(p);
    }

    @Transactional
    public PlaylistResponse update(Long playlistId, PlaylistUpdateRequest req) {
        User me = currentUser();
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!p.getOwner().getId().equals(me.getId())) {
            throw new IllegalArgumentException("Only owner can update this playlist");
        }

        p.setName(req.getName());
        p.setDescription(req.getDescription());
        if (req.getPrivacy() != null) p.setPrivacy(req.getPrivacy());

        return toResponse(playlistRepository.save(p));
    }

    @Transactional
    public void delete(Long playlistId) {
        User me = currentUser();
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!p.getOwner().getId().equals(me.getId())) {
            throw new IllegalArgumentException("Only owner can delete this playlist");
        }

        // delete join rows first
        List<PlaylistSong> items = playlistSongs(p);
        playlistSongRepository.deleteAll(items);

        playlistRepository.delete(p);
    }

    @Transactional
    public PlaylistResponse addSong(Long playlistId, Long songId) {
        User me = currentUser();
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!p.getOwner().getId().equals(me.getId())) {
            throw new IllegalArgumentException("Only owner can add songs");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (playlistSongRepository.existsByPlaylistAndSong(p, song)) {
            return toResponse(p); // already present
        }

        int nextPos = playlistSongRepository.findMaxPosition(p) + 1;

        playlistSongRepository.save(PlaylistSong.builder()
                .playlist(p)
                .song(song)
                .position(nextPos)
                .build());

        return toResponse(p);
    }

    @Transactional
    public PlaylistResponse removeSong(Long playlistId, Long songId) {
        User me = currentUser();
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!p.getOwner().getId().equals(me.getId())) {
            throw new IllegalArgumentException("Only owner can remove songs");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        playlistSongRepository.deleteByPlaylistAndSong(p, song);

        // reindex positions
        List<PlaylistSong> items = playlistSongs(p);
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setPosition(i + 1);
        }
        playlistSongRepository.saveAll(items);

        return toResponse(p);
    }

    @Transactional
    public PlaylistResponse reorder(Long playlistId, ReorderRequest req) {
        User me = currentUser();
        Playlist p = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (!p.getOwner().getId().equals(me.getId())) {
            throw new IllegalArgumentException("Only owner can reorder songs");
        }

        List<PlaylistSong> items = playlistSongs(p);

        if (req.getSongIds() == null || req.getSongIds().isEmpty()) {
            throw new IllegalArgumentException("songIds are required");
        }

        // Validate same songs count
        if (req.getSongIds().size() != items.size()) {
            throw new IllegalArgumentException("Song list size mismatch");
        }

        // ✅ Validate no duplicates in request
        HashSet<Long> unique = new HashSet<>(req.getSongIds());
        if (unique.size() != req.getSongIds().size()) {
            throw new IllegalArgumentException("Duplicate songIds are not allowed");
        }

        // Map by songId
        Map<Long, PlaylistSong> map = new HashMap<>();
        for (PlaylistSong it : items) {
            map.put(it.getSong().getId(), it);
        }

        // ✅ Validate request contains only songs from this playlist
        for (Long songId : req.getSongIds()) {
            if (!map.containsKey(songId)) {
                throw new IllegalArgumentException("Song not in playlist: " + songId);
            }
        }

        // Apply positions
        for (int i = 0; i < req.getSongIds().size(); i++) {
            Long songId = req.getSongIds().get(i);
            PlaylistSong it = map.get(songId);
            it.setPosition(i + 1);
        }

        playlistSongRepository.saveAll(items);
        return toResponse(p);
    }

    // ---------- mapper ----------
    private PlaylistResponse toResponse(Playlist p) {
        List<PlaylistSongResponse> songs = playlistSongs(p)
                .stream()
                .map(ps -> new PlaylistSongResponse(
                        ps.getSong().getId(),
                        ps.getSong().getTitle(),
                        ps.getSong().getAudioUrl(),
                        ps.getSong().getCoverUrl(),
                        ps.getPosition()
                ))
                .toList();

        return new PlaylistResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrivacy(),
                p.getOwner().getId(),
                p.getOwner().getUsername(),
                songs
        );
    }
}