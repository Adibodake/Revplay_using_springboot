package com.revplay.service;

import com.revplay.dto.ArtistDashboardResponse;
import com.revplay.dto.UserStatsResponse;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.User;
import com.revplay.repository.*;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserStatsResponse myStats() {
        User user = currentUser();
        long playlists = playlistRepository.findByOwnerOrderByCreatedAtDesc(user).size();
        long favs = favoriteRepository.countByUser(user);
        long plays = historyRepository.countByUser(user);
        long listeningSeconds = historyRepository.sumListeningSeconds(user);

        return new UserStatsResponse(playlists, favs, plays, listeningSeconds);
    }

    public ArtistDashboardResponse artistDashboard(int topLimit) {
        User user = currentUser();

        ArtistProfile profile = artistProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Artist profile not found"));

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

        return new ArtistDashboardResponse(totalSongs, totalPlays, totalFavorites, topSongs);
    }
}
