package com.revplay.repository;

import com.revplay.entity.Album;
import com.revplay.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtistOrderByCreatedAtDesc(ArtistProfile artist);

    List<Album> findByNameContainingIgnoreCase(String keyword);

    List<Album> findByArtistId(Long artistId);
}
