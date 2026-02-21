package com.revplay.dto;

import java.util.List;

public record SearchResponse(
        List<SearchSong> songs,
        List<SearchArtist> artists,
        List<SearchAlbum> albums,
        List<SearchPlaylist> playlists
) {
    public record SearchSong(Long id, String title, String artistName) {}
    public record SearchArtist(Long id, String artistName) {}
    public record SearchAlbum(Long id, String name) {}
    public record SearchPlaylist(Long id, String name) {}
}