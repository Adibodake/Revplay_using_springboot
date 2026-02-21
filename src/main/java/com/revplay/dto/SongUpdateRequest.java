package com.revplay.dto;

import com.revplay.entity.enums.SongVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongUpdateRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 100)
    private String genre;

    // optional: set/unset album
    private Long albumId; // null => remove from album

    private SongVisibility visibility; // optional (can be null)
}