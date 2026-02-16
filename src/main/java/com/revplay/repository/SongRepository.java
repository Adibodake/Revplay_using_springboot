package com.revplay.repository;

import com.revplay.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongRepository extends JpaRepository<Song, Long> {

    Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
