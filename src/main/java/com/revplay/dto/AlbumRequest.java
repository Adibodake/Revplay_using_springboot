package com.revplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AlbumRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 800)
    private String description;

    private LocalDate releaseDate;

    @Size(max = 600)
    private String coverUrl;
}
