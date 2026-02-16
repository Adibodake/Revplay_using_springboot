package com.revplay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongUploadRequest {
    private String title;
    private String genre;
    private Integer durationSec; // can pass from UI; later you can compute
}
