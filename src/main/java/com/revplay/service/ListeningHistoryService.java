package com.revplay.service;

import com.revplay.dto.HistoryResponse;
import com.revplay.entity.ListeningHistory;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.repository.ListeningHistoryRepository;
import com.revplay.repository.SongRepository;
import com.revplay.repository.UserRepository;
import com.revplay.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListeningHistoryService {

    private final ListeningHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    private User currentUser() {
        String username = SecurityUtil.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public void trackPlay(Long songId) {
        User user = currentUser();
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        ListeningHistory entry = ListeningHistory.builder()
                .user(user)
                .song(song)
                .build();

        // ✅ if entity doesn't set playedAt, we ensure it
        if (entry.getPlayedAt() == null) {
            entry.setPlayedAt(LocalDateTime.now());
        }

        historyRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> recent() {
        User user = currentUser();
        return historyRepository.findTop50ByUserOrderByPlayedAtDesc(user)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ✅ NEW: recent distinct songs (no duplicates)
    @Transactional(readOnly = true)
    public List<HistoryResponse> recentDistinct(int limit) {
        User user = currentUser();
        return historyRepository.recentDistinctSongs(user.getId(), limit)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<HistoryResponse> all(int page, int size) {
        User user = currentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "playedAt"));
        return historyRepository.findByUserOrderByPlayedAtDesc(user, pageable)
                .map(this::toDto);
    }

    @Transactional
    public void clear() {
        User user = currentUser();
        historyRepository.deleteByUser(user);
    }

    private HistoryResponse toDto(ListeningHistory h) {
        return new HistoryResponse(
                h.getId(),
                h.getSong().getId(),
                h.getSong().getTitle(),
                h.getSong().getAudioUrl(),
                h.getSong().getCoverUrl(),
                h.getPlayedAt(),
                // ✅ new fields (optional but recommended)
                h.getSong().getArtist() != null ? h.getSong().getArtist().getArtistName() : null,
                h.getSong().getDurationSec()
        );
    }
}