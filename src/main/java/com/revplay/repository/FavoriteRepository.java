package com.revplay.repository;

import com.revplay.entity.Favorite;
import com.revplay.entity.Song;
import com.revplay.entity.User;
import com.revplay.entity.keys.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    boolean existsByUserAndSong(User user, Song song);

    void deleteByUserAndSong(User user, Song song);

    List<Favorite> findByUserOrderByCreatedAtDesc(User user);
}
