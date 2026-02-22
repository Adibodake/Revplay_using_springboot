package com.revplay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "playlist_follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "playlist_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    // keeping your original field
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    // ✅ map properly
    @Column(name = "followed_at", nullable = false, updatable = false)
    private LocalDateTime followedAt;

    @PrePersist
    void onFollow() {
        followedAt = LocalDateTime.now();
    }
}