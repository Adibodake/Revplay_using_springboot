package com.revplay.service;

import com.revplay.entity.*;
import com.revplay.repository.*;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final ArtistFollowRepository artistFollowRepository;
    private final PlaylistFollowRepository playlistFollowRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ---------------- ARTIST ----------------

    @Transactional
    public String followArtist(Long artistId) {
        User user = currentUser();
        ArtistProfile artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        if (artistFollowRepository.findByUserAndArtist(user, artist).isPresent())
            return "Already following";

        artistFollowRepository.save(
                ArtistFollow.builder()
                        .user(user)
                        .artist(artist)
                        .build()
        );

        return "Artist followed";
    }

    @Transactional
    public String unfollowArtist(Long artistId) {
        User user = currentUser();
        ArtistProfile artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        artistFollowRepository.findByUserAndArtist(user, artist)
                .ifPresent(artistFollowRepository::delete);

        return "Artist unfollowed";
    }

    public List<ArtistProfile> myFollowedArtists() {
        User user = currentUser();
        return artistFollowRepository.findByUser(user)
                .stream()
                .map(ArtistFollow::getArtist)
                .toList();
    }

    // ---------------- PLAYLIST ----------------

    @Transactional
    public String followPlaylist(Long playlistId) {
        User user = currentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        if (playlistFollowRepository.findByUserAndPlaylist(user, playlist).isPresent())
            return "Already following";

        playlistFollowRepository.save(
                PlaylistFollow.builder()
                        .user(user)
                        .playlist(playlist)
                        .build()
        );

        return "Playlist followed";
    }

    @Transactional
    public String unfollowPlaylist(Long playlistId) {
        User user = currentUser();
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        playlistFollowRepository.findByUserAndPlaylist(user, playlist)
                .ifPresent(playlistFollowRepository::delete);

        return "Playlist unfollowed";
    }

    public List<Playlist> myFollowedPlaylists() {
        User user = currentUser();
        return playlistFollowRepository.findByUser(user)
                .stream()
                .map(PlaylistFollow::getPlaylist)
                .toList();
    }
}