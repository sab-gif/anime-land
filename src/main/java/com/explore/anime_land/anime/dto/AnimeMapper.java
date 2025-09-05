package com.explore.anime_land.anime.dto;

import com.explore.anime_land.anime.Anime;
import com.explore.anime_land.users.dto.UserMapper;
import com.explore.anime_land.users.dto.UserResponseShort;
import com.explore.anime_land.users.User;

public class AnimeMapper {
    public static Anime toEntity(AnimeRequest dto, User user){
        return Anime.builder()
                .title(dto.title())
                .genre(dto.genre())
                .description(dto.description())
                .imageUrl(dto.imageUrl())
                .user(user)
                .build();
    }
    public static AnimeResponse toDto(Anime anime){
        UserResponseShort userDto = UserMapper.toDtoShort(anime.getUser());
        return new AnimeResponse(anime.getId(), anime.getTitle(), anime.getGenre(), anime.getDescription(), anime.getImageUrl(), userDto);
    }
    public static AnimeResponseShort toDtoShort (Anime anime){
        UserResponseShort userDto = UserMapper.toDtoShort(anime.getUser());
        return new AnimeResponseShort(anime.getId(), anime.getTitle(), anime.getGenre(), anime.getImageUrl(), userDto);
    }

}
