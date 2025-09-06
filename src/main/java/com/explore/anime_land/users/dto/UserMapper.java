package com.explore.anime_land.users.dto;

import com.explore.anime_land.users.Role;
import com.explore.anime_land.users.User;

public class UserMapper {
    public static User toEntity(UserRegisterRequest dto, Role role) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .role(role)
                .build();
    }

    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public static UserResponseShort toDtoShort (User user){
        return new UserResponseShort(user.getUsername());
    }

}
