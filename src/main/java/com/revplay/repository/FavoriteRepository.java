package com.revplay.repository;

import com.revplay.entity.Favorite;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.entity.keys.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    boolean existsByUserAndSong(User user, Song song);

    Optional<Favorite> findByUserAndSong(User user, Song song); // ✅ recommended

    void deleteByUserAndSong(User user, Song song);

    List<Favorite> findByUserOrderByCreatedAtDesc(User user);

    long countByUser(User user);

    // ✅ Artist analytics (kept as-is)
    @Query("select count(f) from Favorite f where f.song.artist.user.id = :artistUserId")
    long countFavoritesForArtistUser(@Param("artistUserId") Long artistUserId);

    @Query("""
       select distinct f.user.id, f.user.username
       from Favorite f
       where f.song.artist.user.id = :artistUserId
       order by f.user.username
       """)
    List<Object[]> favoritersForArtist(@Param("artistUserId") Long artistUserId);

    @Query("""
       select distinct f.user.id, f.user.username
       from Favorite f
       where f.song.artist.user.id = :artistUserId
         and f.song.id = :songId
       order by f.user.username
       """)
    List<Object[]> favoritersForArtistSong(
            @Param("artistUserId") Long artistUserId,
            @Param("songId") Long songId
    );
}