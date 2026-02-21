package com.revplay.service;

import com.revplay.dto.ArtistAlbumResponse;
import com.revplay.dto.ArtistPublicResponse;
import com.revplay.dto.ArtistSongResponse;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.Song;
import com.revplay.repository.AlbumRepository;
import com.revplay.repository.ArtistProfileRepository;
import com.revplay.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistPublicService {

    private final ArtistProfileRepository artistProfileRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public ArtistPublicResponse getArtist(Long artistId) {
        ArtistProfile a = artistProfileRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found"));

        return new ArtistPublicResponse(
                a.getId(),
                a.getArtistName(),
                a.getBio(),
                a.getGenre(),
                a.getProfilePicUrl(),
                a.getBannerUrl(),
                a.getInstagramUrl(),
                a.getTwitterUrl(),
                a.getYoutubeUrl(),
                a.getSpotifyUrl(),
                a.getWebsiteUrl()
        );
    }

    public List<ArtistSongResponse> artistSongs(Long artistId) {
        return songRepository.findByArtistId(artistId)
                .stream()
                .map(this::toSong)
                .toList();
    }

    public List<ArtistAlbumResponse> artistAlbums(Long artistId) {
        return albumRepository.findByArtistId(artistId)
                .stream()
                .map(a -> new ArtistAlbumResponse(
                        a.getId(),
                        a.getName(),
                        a.getDescription(),
                        a.getReleaseDate(),
                        a.getCoverUrl()
                ))
                .toList();
    }

    private ArtistSongResponse toSong(Song s) {
        return new ArtistSongResponse(
                s.getId(),
                s.getTitle(),
                s.getGenre(),
                s.getDurationSec(),
                s.getVisibility(),
                s.getReleaseDate(),
                s.getAudioUrl(),
                s.getCoverUrl(),
                s.getAlbum() != null ? s.getAlbum().getId() : null
        );
    }
}