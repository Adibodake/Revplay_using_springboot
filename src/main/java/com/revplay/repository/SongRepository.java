package com.revplay.repository;

import com.revplay.entity.Album;
import com.revplay.entity.ArtistProfile;
import com.revplay.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SongRepository extends JpaRepository<Song, Long> {

    Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    long countByArtist(ArtistProfile artist);

    long countByAlbum(Album album);

    List<Song> findByAlbumOrderByIdAsc(Album album);

    List<Song> findByArtistOrderByIdAsc(ArtistProfile artist);

    List<Song> findByTitleContainingIgnoreCase(String keyword);

    List<Song> findByGenreIgnoreCase(String genre);

    List<Song> findByArtistId(Long artistId);

    List<Song> findByAlbumId(Long albumId);

    @Query("select s from Song s where year(s.releaseDate) = :year")
    List<Song> findByReleaseYear(@Param("year") int year);



}
