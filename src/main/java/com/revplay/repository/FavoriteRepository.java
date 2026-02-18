package com.revplay.repository;

import com.revplay.entity.Favorite;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.entity.keys.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    boolean existsByUserAndSong(User user, Song song);

    void deleteByUserAndSong(User user, Song song);

    List<Favorite> findByUserOrderByCreatedAtDesc(User user);

    long countByUser(User user);

//    @Query("select count(f) from Favorite f where f.song.artist.user.id = :artistUserId")
//    long countFavoritesForArtistUser(Long artistUserId);

    @Query("select count(f) from Favorite f where f.song.artist.user.id = :artistUserId")
    long countFavoritesForArtistUser(@Param("artistUserId") Long artistUserId);

}
