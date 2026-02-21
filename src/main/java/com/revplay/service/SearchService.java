package com.revplay.service;

import com.revplay.dto.SearchResponse;
import com.revplay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SongRepository songRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final AlbumRepository albumRepository;
    private final PlaylistRepository playlistRepository;

    public SearchResponse search(String keyword) {

        var songs = songRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(s -> new SearchResponse.SearchSong(
                        s.getId(),
                        s.getTitle(),
                        s.getArtist() != null ? s.getArtist().getArtistName() : null
                ))
                .toList();

        var artists = artistProfileRepository.findByArtistNameContainingIgnoreCase(keyword)
                .stream()
                .map(a -> new SearchResponse.SearchArtist(
                        a.getId(),
                        a.getArtistName()
                ))
                .toList();

        var albums = albumRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(a -> new SearchResponse.SearchAlbum(
                        a.getId(),
                        a.getName()
                ))
                .toList();

        var playlists = playlistRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(p -> new SearchResponse.SearchPlaylist(
                        p.getId(),
                        p.getName()
                ))
                .toList();

        return new SearchResponse(
                songs,
                artists,
                albums,
                playlists
        );
    }
}