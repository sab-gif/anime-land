package com.explore.anime_land.anime.dto;

import com.explore.anime_land.users.dto.UserResponseShort;

public record AnimeResponseShort(
        Long id,
        String title,
        String genre,
        String image,
        UserResponseShort user
) {
}
