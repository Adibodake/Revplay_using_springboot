package com.revplay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "playlist_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    private LocalDateTime followedAt;

    @PrePersist
    void onFollow() {
        followedAt = LocalDateTime.now();
    }
}