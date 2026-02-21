package com.revplay.repository;

import com.revplay.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistFollowRepository extends JpaRepository<PlaylistFollow, Long> {

    Optional<PlaylistFollow> findByUserAndPlaylist(User user, Playlist playlist);

    List<PlaylistFollow> findByUser(User user);

    long countByPlaylist(Playlist playlist);
}