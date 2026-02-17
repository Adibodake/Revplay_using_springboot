package com.revplay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "artist_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one user has one artist profile
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "artist_name", nullable = false, length = 120)
    private String artistName;

    @Column(length = 500)
    private String bio;

    @Column(length = 60)
    private String genre;

    @Column(name = "profile_pic_url", length = 600)
    private String profilePicUrl;

    @Column(name = "banner_url", length = 600)
    private String bannerUrl;

    // social links
    @Column(name = "instagram_url", length = 600)
    private String instagramUrl;

    @Column(name = "twitter_url", length = 600)
    private String twitterUrl;

    @Column(name = "youtube_url", length = 600)
    private String youtubeUrl;

    @Column(name = "spotify_url", length = 600)
    private String spotifyUrl;

    @Column(name = "website_url", length = 600)
    private String websiteUrl;

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
