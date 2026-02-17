package com.revplay.repository;

import com.revplay.entity.Playlist;
import com.revplay.entity.PlaylistSong;
import com.revplay.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findByPlaylistOrderByPositionAsc(Playlist playlist);

    boolean existsByPlaylistAndSong(Playlist playlist, Song song);

    void deleteByPlaylistAndSong(Playlist playlist, Song song);

    @Query("select coalesce(max(ps.position), 0) from PlaylistSong ps where ps.playlist = :playlist")
    int findMaxPosition(Playlist playlist);
}
