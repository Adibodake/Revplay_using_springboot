package com.revplay.dto;

import com.revplay.entity.enums.PlaylistPrivacy;

import java.util.List;

public record PlaylistResponse(
        Long id,
        String name,
        String description,
        PlaylistPrivacy privacy,
        Long ownerId,
        String ownerUsername,
        List<PlaylistSongResponse> songs
) {}
