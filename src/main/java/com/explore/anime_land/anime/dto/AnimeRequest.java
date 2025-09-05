package com.explore.anime_land.anime.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AnimeRequest(
        @Schema(description = "Title", example = "Jujutsu Kaisen", required = true)
        @NotBlank(message = "Title is required")
        @Size(min=5, max=15, message = "Title must be more than 5 characters and less than 15 characters")
        String title,

        @Schema(description = "genre", example = "Darkfantasy, action", required = true)
        @NotBlank (message = "Genre is required")
        @Size(min=8, max=20, message = "Genre must be more than 8 characters and less than 30 characters")
        String genre,

        @Schema(description = "Description", example = "Jujutsu Kaisen follows high school student Yuji Itadori, who becomes the host to a powerful demon named Sukuna after swallowing one of his fingers, and then joins a secret organization of Jujutsu Sorcerers to help control Sukuna's power and eliminate powerful Curses born from negative human emotions")
        @Size(max=500, message = "Description must be less than 500 characters")
        String description,

        @Schema(description = "ImageUrl", example = "https://example.com/image.jpg")
        @Pattern(message = "Invalid content type", regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|svg))$")
        String imageUrl
) {
}
