package com.revplay.repository;

import com.revplay.entity.ArtistProfile;
import com.revplay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    Optional<ArtistProfile> findByUser(User user);
}
