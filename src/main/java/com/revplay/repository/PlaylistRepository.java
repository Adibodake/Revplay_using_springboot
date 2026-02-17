package com.revplay.repository;

import com.revplay.entity.Playlist;
import com.revplay.entity.User;
import com.revplay.entity.enums.PlaylistPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByOwnerOrderByCreatedAtDesc(User owner);
    List<Playlist> findByPrivacyOrderByCreatedAtDesc(PlaylistPrivacy privacy);
}
