package com.revplay.dto;

import jakarta.validation.constraints.NotBlank;

public class AlbumCreateRequest {

    @NotBlank
    private String name;

    private String description;

    // "2026-02-22"
    private String releaseDate;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getReleaseDate() { return releaseDate; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}