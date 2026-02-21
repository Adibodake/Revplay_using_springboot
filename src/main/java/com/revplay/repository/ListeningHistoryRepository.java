package com.revplay.repository;

import com.revplay.entity.ListeningHistory;
import com.revplay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    // Full history (latest first)
    Page<ListeningHistory> findByUserOrderByPlayedAtDesc(User user, Pageable pageable);

    // Recent 50
    List<ListeningHistory> findTop50ByUserOrderByPlayedAtDesc(User user);

    // Clear history
    void deleteByUser(User user);

    // Listener stats
    long countByUser(User user);

    @Query("select coalesce(sum(h.song.durationSec), 0) from ListeningHistory h where h.user = :user")
    long sumListeningSeconds(@Param("user") User user);

    // Artist analytics
    @Query("select count(h) from ListeningHistory h where h.song.artist.user.id = :artistUserId")
    long countPlaysForArtistUser(@Param("artistUserId") Long artistUserId);

    @Query("""
           select h.song.id, h.song.title, count(h) as plays
           from ListeningHistory h
           where h.song.artist.user.id = :artistUserId
           group by h.song.id, h.song.title
           order by plays desc
           """)
    List<Object[]> topSongsForArtistUser(@Param("artistUserId") Long artistUserId, Pageable pageable);

    @Query("""
           select h.user.id, h.user.username, count(h) as plays
           from ListeningHistory h
           where h.song.artist.user.id = :artistUserId
           group by h.user.id, h.user.username
           order by plays desc
           """)
    List<Object[]> topListeners(@Param("artistUserId") Long artistUserId, Pageable pageable);
}