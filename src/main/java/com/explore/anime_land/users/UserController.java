package com.explore.anime_land.users;

import com.explore.anime_land.anime.AnimeService;
import com.explore.anime_land.anime.dto.AnimeResponse;
import com.explore.anime_land.common.SecuredBaseController;
import com.explore.anime_land.security.CustomUserDetail;
import com.explore.anime_land.users.dto.UserRegisterRequest;
import com.explore.anime_land.users.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends SecuredBaseController {

    private final UserService userService;
    private final AnimeService animeService;

    @Operation(
            summary = "Get user by authentication",
            description = "Returns a user by authentication. Throws error if not found."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        UserResponse user = userService.getOwnUser(userDetail.getId());
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get anime by authenticated user",
            description = "Return anime by authenticated user. Throws error if not found."
    )
    @GetMapping("/me/animes")
    public ResponseEntity<List<AnimeResponse>> getMyAnimes(@AuthenticationPrincipal CustomUserDetail userDetail) {
        List<AnimeResponse> list = animeService.getAnimesByUsername(userDetail.getUser().getUsername());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Update authenticated user",
            description = "Updates the authenticated user's username, email or password."
    )
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid UserRegisterRequest userRequest
    ){
        UserResponse updateUser =userService.updateOwnUser(userDetail.getId(), userRequest);
        return ResponseEntity.ok(updateUser);
    }

    @Operation(
            summary = "Delete authenticated user",
            description = "Deletes authenticated user. Returns a message if successful."
    )
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        String message = userService.deleteOwnUser(userDetail.getId());
        return ResponseEntity.ok(message);
    }
}
