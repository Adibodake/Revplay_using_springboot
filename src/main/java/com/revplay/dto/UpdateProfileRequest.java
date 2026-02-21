package com.revplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank
    @Size(max = 150)
    private String displayName;

    @Size(max = 500)
    private String bio;
}