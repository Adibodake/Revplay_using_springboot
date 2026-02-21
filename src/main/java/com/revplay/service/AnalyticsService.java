package com.revplay.service;

import com.revplay.dto.ArtistDashboardResponse;
import com.revplay.dto.TopListenerResponse;
import com.revplay.dto.UserStatsResponse;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.User;
import com.revplay.repository.*;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.revplay.dto.FavoriterResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final ListeningHistoryRepository historyRepository;

    private final ArtistProfileRepository artistProfileRepository;
    private final SongRepository songRepository;
    private final ArtistFollowRepository artistFollowRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ✅ Listener stats
    public UserStatsResponse myStats() {
        User user = currentUser();

        long playlists = playlistRepository.findByOwnerOrderByCreatedAtDesc(user).size();
        long favs = favoriteRepository.countByUser(user);
        long plays = historyRepository.countByUser(user);
        long listeningSeconds = historyRepository.sumListeningSeconds(user);

        return new UserStatsResponse(playlists, favs, plays, listeningSeconds);
    }

    // ✅ Artist dashboard (+ follower count)
    public ArtistDashboardResponse artistDashboard(int topLimit) {
        User user = currentUser();

        ArtistProfile profile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));

        long followerCount = artistFollowRepository.countByArtist(profile);

        long totalSongs = songRepository.countByArtist(profile);
        long totalPlays = historyRepository.countPlaysForArtistUser(user.getId());
        long totalFavorites = favoriteRepository.countFavoritesForArtistUser(user.getId());

        List<ArtistDashboardResponse.TopSong> topSongs =
                historyRepository.topSongsForArtistUser(user.getId(), PageRequest.of(0, topLimit))
                        .stream()
                        .map(row -> new ArtistDashboardResponse.TopSong(
                                ((Number) row[0]).longValue(),
                                (String) row[1],
                                ((Number) row[2]).longValue()
                        ))
                        .toList();

        return new ArtistDashboardResponse(
                totalSongs,
                totalPlays,
                totalFavorites,
                followerCount,
                topSongs
        );
    }

    // ✅ Top listeners for my songs (ARTIST)
    public List<TopListenerResponse> topListeners(int limit) {
        User user = currentUser();

        return historyRepository.topListeners(user.getId(), PageRequest.of(0, limit))
                .stream()
                .map(r -> new TopListenerResponse(
                        ((Number) r[0]).longValue(),
                        (String) r[1],
                        ((Number) r[2]).longValue()
                ))
                .toList();
    }

    public List<FavoriterResponse> favoriters(Long songId) {
        User artistUser = currentUser();

        List<Object[]> rows = (songId == null)
                ? favoriteRepository.favoritersForArtist(artistUser.getId())
                : favoriteRepository.favoritersForArtistSong(artistUser.getId(), songId);

        return rows.stream()
                .map(r -> new FavoriterResponse(
                        ((Number) r[0]).longValue(),
                        (String) r[1]
                ))
                .toList();
    }
}