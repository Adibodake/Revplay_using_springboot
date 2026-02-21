package com.revplay.repository;

import com.revplay.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistFollowRepository extends JpaRepository<ArtistFollow, Long> {

    Optional<ArtistFollow> findByUserAndArtist(User user, ArtistProfile artist);

    List<ArtistFollow> findByUser(User user);

    long countByArtist(ArtistProfile artist);
}