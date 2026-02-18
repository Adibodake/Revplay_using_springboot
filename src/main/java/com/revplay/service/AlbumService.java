package com.revplay.service;

import com.revplay.dto.AlbumRequest;
import com.revplay.dto.AlbumResponse;
import com.revplay.entity.*;
import com.revplay.repository.*;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final UserRepository userRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    private ArtistProfile currentArtist() {
        String username = SecurityUtil.currentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));
    }

    // ARTIST: create
    public AlbumResponse create(AlbumRequest req) {
        ArtistProfile artist = currentArtist();

        Album album = Album.builder()
                .artist(artist)
                .name(req.getName())
                .description(req.getDescription())
                .releaseDate(req.getReleaseDate())
                .coverUrl(req.getCoverUrl())
                .build();

        return toResponse(albumRepository.save(album));
    }

    // ARTIST: list mine
    public List<AlbumResponse> myAlbums() {
        ArtistProfile artist = currentArtist();
        return albumRepository.findByArtistOrderByCreatedAtDesc(artist)
                .stream().map(this::toResponse).toList();
    }

    // PUBLIC: list all (simple)
    public List<AlbumResponse> allAlbums() {
        return albumRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    // PUBLIC: details
    public AlbumResponse get(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));
        return toResponse(album);
    }

    // ARTIST: update
    public AlbumResponse update(Long id, AlbumRequest req) {
        ArtistProfile artist = currentArtist();
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (!album.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You cannot edit this album");
        }

        album.setName(req.getName());
        album.setDescription(req.getDescription());
        album.setReleaseDate(req.getReleaseDate());
        album.setCoverUrl(req.getCoverUrl());

        return toResponse(albumRepository.save(album));
    }

    // ARTIST: delete only if no songs
    @Transactional
    public void delete(Long id) {
        ArtistProfile artist = currentArtist();
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (!album.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You cannot delete this album");
        }

        long songsCount = songRepository.countByAlbum(album);
        if (songsCount > 0) {
            throw new IllegalArgumentException("Cannot delete album: remove songs first");
        }

        albumRepository.delete(album);
    }

    // ARTIST: add song to album (song must belong to same artist)
    @Transactional
    public AlbumResponse addSong(Long albumId, Long songId) {
        ArtistProfile artist = currentArtist();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (!album.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You cannot modify this album");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (song.getArtist() == null || !song.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You can only add your own songs");
        }

        song.setAlbum(album);
        songRepository.save(song);

        return toResponse(album);
    }

    // ARTIST: remove song from album
    @Transactional
    public AlbumResponse removeSong(Long albumId, Long songId) {
        ArtistProfile artist = currentArtist();
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        if (!album.getArtist().getId().equals(artist.getId())) {
            throw new IllegalArgumentException("You cannot modify this album");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (song.getAlbum() == null || !song.getAlbum().getId().equals(album.getId())) {
            throw new IllegalArgumentException("Song is not in this album");
        }

        song.setAlbum(null);
        songRepository.save(song);

        return toResponse(album);
    }

    // mapper
    private AlbumResponse toResponse(Album album) {
        List<AlbumResponse.TrackResponse> tracks = songRepository.findByAlbumOrderByIdAsc(album)
                .stream()
                .map(s -> new AlbumResponse.TrackResponse(
                        s.getId(),
                        s.getTitle(),
                        s.getDurationSec(),
                        s.getAudioUrl(),
                        s.getCoverUrl()
                ))
                .toList();

        return new AlbumResponse(
                album.getId(),
                album.getName(),
                album.getDescription(),
                album.getReleaseDate(),
                album.getCoverUrl(),
                album.getArtist().getId(),
                album.getArtist().getArtistName(),
                tracks
        );
    }
}
