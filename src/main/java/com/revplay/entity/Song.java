package com.revplay.entity;

import com.revplay.entity.enums.SongVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For now keep artist/album as null until you create ArtistProfile/Album entities
    // Later we will add:
    // @ManyToOne @JoinColumn(name="artist_id") private ArtistProfile artist;
    // @ManyToOne @JoinColumn(name="album_id")  private Album album;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 60)
    private String genre;

    @Column(name = "duration_sec", nullable = false)
    private int durationSec;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SongVisibility visibility = SongVisibility.PUBLIC;

    @Column(name = "audio_url", nullable = false, length = 600)
    private String audioUrl;

    @Column(name = "cover_url", length = 600)
    private String coverUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
