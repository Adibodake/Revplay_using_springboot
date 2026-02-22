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

    // Recent 50 (can contain duplicates)
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

    // ✅ NEW: Recent distinct songs (latest play per song)
    // NOTE: Ensure your DB table/columns match:
    // listening_history, user_id, song_id, played_at
    @Query(value = """
        SELECT h.*
        FROM listening_history h
        JOIN (
            SELECT song_id, MAX(played_at) AS max_played
            FROM listening_history
            WHERE user_id = :userId
            GROUP BY song_id
            ORDER BY max_played DESC
            LIMIT :limit
        ) t ON t.song_id = h.song_id AND t.max_played = h.played_at
        WHERE h.user_id = :userId
        ORDER BY h.played_at DESC
        """, nativeQuery = true)
    List<ListeningHistory> recentDistinctSongs(@Param("userId") Long userId, @Param("limit") int limit);

    // ✅ DAILY (MySQL): group by DATE(played_at)
    @Query(value = """
        select date(h.played_at) as bucketKey, count(*) as plays
        from listening_history h
        join songs s on s.id = h.song_id
        join artist_profiles ap on ap.id = s.artist_profile_id
        where ap.user_id = :artistUserId
          and h.played_at >= (now() - interval :days day)
        group by date(h.played_at)
        order by bucketKey
        """, nativeQuery = true)
    List<Object[]> artistTrendsDaily(@Param("artistUserId") Long artistUserId,
                                     @Param("days") int days);

    // ✅ WEEKLY (MySQL): year-week (mode 3 ISO-like)
    @Query(value = """
        select concat(year(h.played_at), '-W', lpad(week(h.played_at, 3), 2, '0')) as bucketKey,
               count(*) as plays
        from listening_history h
        join songs s on s.id = h.song_id
        join artist_profiles ap on ap.id = s.artist_profile_id
        where ap.user_id = :artistUserId
          and h.played_at >= (now() - interval :days day)
        group by year(h.played_at), week(h.played_at, 3)
        order by year(h.played_at), week(h.played_at, 3)
        """, nativeQuery = true)
    List<Object[]> artistTrendsWeekly(@Param("artistUserId") Long artistUserId,
                                      @Param("days") int days);

    // ✅ MONTHLY (MySQL): YYYY-MM
    @Query(value = """
        select date_format(h.played_at, '%Y-%m') as bucketKey, count(*) as plays
        from listening_history h
        join songs s on s.id = h.song_id
        join artist_profiles ap on ap.id = s.artist_profile_id
        where ap.user_id = :artistUserId
          and h.played_at >= (now() - interval :days day)
        group by date_format(h.played_at, '%Y-%m')
        order by bucketKey
        """, nativeQuery = true)
    List<Object[]> artistTrendsMonthly(@Param("artistUserId") Long artistUserId,
                                       @Param("days") int days);

    @Query("""
       select h.song.id, h.song.title, count(h) as plays
       from ListeningHistory h
       group by h.song.id, h.song.title
       order by plays desc
       """)
    List<Object[]> trendingSongs(Pageable pageable);
}