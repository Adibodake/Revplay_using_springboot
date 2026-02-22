//package com.revplay.service;
//
//import com.revplay.entity.Favorite;
//import com.revplay.entity.Song;
//import com.revplay.entity.User;
//import com.revplay.repository.FavoriteRepository;
//import com.revplay.repository.SongRepository;
//import com.revplay.repository.UserRepository;
//import com.revplay.security.SecurityUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FavoriteService {
//
//    private final FavoriteRepository favoriteRepository;
//    private final UserRepository userRepository;
//    private final SongRepository songRepository;
//
//    private User currentUser() {
//        String username = SecurityUtil.currentUsername();
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//    }
//
//    public void addFavorite(Long songId) {
//        User user = currentUser();
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new IllegalArgumentException("Song not found"));
//
//        if (favoriteRepository.existsByUserAndSong(user, song)) return;
//
//        favoriteRepository.save(Favorite.builder()
//                .user(user)
//                .song(song)
//                .build());
//    }
//
//    public void removeFavorite(Long songId) {
//        User user = currentUser();
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new IllegalArgumentException("Song not found"));
//
//        favoriteRepository.deleteByUserAndSong(user, song);
//    }
//
//    public List<Favorite> myFavorites() {
//        User user = currentUser();
//        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
//    }
//}

package com.revplay.service;

import com.revplay.dto.FavoriteSongResponse;
import com.revplay.entity.Favorite;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.repository.FavoriteRepository;
import com.revplay.repository.SongRepository;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void addFavorite(Long songId) {
        User user = currentUser();
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        if (favoriteRepository.existsByUserAndSong(user, song)) return;

        favoriteRepository.save(Favorite.builder()
                .user(user)
                .song(song)
                .build());
    }

    @Transactional
    public void removeFavorite(Long songId) {
        User user = currentUser();
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        favoriteRepository.deleteByUserAndSong(user, song);
    }

    // ✅ return DTO list instead of Entity list
    public List<FavoriteSongResponse> myFavorites() {
        User user = currentUser();

        return favoriteRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private FavoriteSongResponse toDto(Favorite f) {
        return new FavoriteSongResponse(
                f.getSong().getId(),
                f.getSong().getTitle(),
                f.getSong().getArtist() != null ? f.getSong().getArtist().getArtistName() : null,
                f.getSong().getDurationSec(),
                f.getSong().getAudioUrl(),
                f.getSong().getCoverUrl(),
                f.getCreatedAt()
        );
    }
}