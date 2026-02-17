package com.revplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistProfileRequest {

    @NotBlank
    @Size(max = 120)
    private String artistName;

    @Size(max = 500)
    private String bio;

    @Size(max = 60)
    private String genre;

    @Size(max = 600)
    private String profilePicUrl;

    @Size(max = 600)
    private String bannerUrl;

    @Size(max = 600)
    private String instagramUrl;

    @Size(max = 600)
    private String twitterUrl;

    @Size(max = 600)
    private String youtubeUrl;

    @Size(max = 600)
    private String spotifyUrl;

    @Size(max = 600)
    private String websiteUrl;
}
