package com.revplay.dto;

public record ArtistPublicResponse(
        Long id,
        String artistName,
        String bio,
        String genre,
        String profilePicUrl,
        String bannerUrl,
        String instagramUrl,
        String twitterUrl,
        String youtubeUrl,
        String spotifyUrl,
        String websiteUrl
) {}