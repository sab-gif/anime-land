package com.explore.anime_land.anime.dto;

import com.explore.anime_land.users.dto.UserResponseShort;

public record AnimeResponse(
        Long id,
        String title,
        String genre,
        String description,
        String image,
        UserResponseShort user
) {
}
