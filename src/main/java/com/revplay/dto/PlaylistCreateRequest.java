package com.revplay.dto;

import com.revplay.entity.enums.PlaylistPrivacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistCreateRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;

    private PlaylistPrivacy privacy = PlaylistPrivacy.PRIVATE;
}
