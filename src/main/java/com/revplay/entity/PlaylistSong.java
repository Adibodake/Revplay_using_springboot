package com.revplay.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "playlist_songs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_playlist_song", columnNames = {"playlist_id", "song_id"}),
                @UniqueConstraint(name = "uk_playlist_position", columnNames = {"playlist_id", "position"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(optional = false)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(nullable = false)
    private int position; // 1..N


}
