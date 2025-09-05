package com.explore.anime_land.anime;

import com.explore.anime_land.anime.dto.AnimeRequest;
import com.explore.anime_land.anime.dto.AnimeResponse;
import com.explore.anime_land.anime.dto.AnimeResponseShort;
import com.explore.anime_land.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @Operation(
            summary = "Get all animes",
            description = "Returns all animes."
    )
    @GetMapping
    public ResponseEntity<List<AnimeResponseShort>> getAllAnimes() {
        List<AnimeResponseShort> list = animeService.getAllAnimes();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Get all animes sorted by User",
            description = "Returns all animes sorted by User."
    )
    @GetMapping("/auth")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public  ResponseEntity<List<AnimeResponseShort>> getAllAnimesUsers(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        List<AnimeResponseShort> list = animeService.getAllAnimesUser(userDetail.getUsername());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Get animes by filter",
            description = "Returns animes filtered by titlte, genre. Returns empty list if none found"
    )
    @GetMapping("/filter")
    public ResponseEntity<List<AnimeResponse>> getFilteredAnimes ( @RequestParam(required = false) String genre, @RequestParam (required = false) String title) {
        List<AnimeResponse> list = animeService.getFilteredAnimes(genre, title);
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Get anime by ID",
            description = "Returns an anime by its ID. Throws error if not found"
    )
    @GetMapping("/{animeId}")
    public ResponseEntity<AnimeResponse> getAnimeById(@PathVariable Long animeId) {
        AnimeResponse anime = animeService.getAnimeById(animeId);
        return  ResponseEntity.ok(anime);
    }

    @Operation(
            summary = "Add anime with authenticated user",
            description = "Creats anime asociated with the authenticated user"
    )
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AnimeResponse> addAdmin (@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AnimeRequest request) {
        AnimeResponse created = animeService.addAnime(request, userDetail.getUser());
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Update anime by ID with authenticated user",
            description = "Updates anime by ID if user is authenticated and has permissions."
    )
    @PutMapping("/{animeId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AnimeResponse> updateAnime (@AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable Long animeId, @RequestBody @Valid AnimeRequest request){
        AnimeResponse updated = animeService.updateAnime(animeId, request, userDetail.getUser());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete anime by ID with authenticated user",
            description = "Deletes anime by ID if user is authenticated and has permissions"
    )
    @DeleteMapping("/{animeId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteAnime(@AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable Long animeId){
        String message = animeService.deleteAnime(animeId, userDetail.getUser());
        return ResponseEntity.ok(message);
    }

}
