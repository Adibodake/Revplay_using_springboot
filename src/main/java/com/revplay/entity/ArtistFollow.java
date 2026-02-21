package com.revplay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "artist_follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "artist_profile_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    private ArtistProfile artist;

    private LocalDateTime followedAt;

    @PrePersist
    void onFollow() {
        followedAt = LocalDateTime.now();
    }
}