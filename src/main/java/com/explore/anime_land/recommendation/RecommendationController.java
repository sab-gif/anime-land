package com.explore.anime_land.recommendation;

import com.explore.anime_land.recommendation.dto.RecommendationRequest;
import com.explore.anime_land.recommendation.dto.RecommendationResponse;
import com.explore.anime_land.recommendation.dto.RecommendationResponseShort;
import com.explore.anime_land.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
            summary = "Get all recommendation",
            description = "Returns all recommendations."
    )
    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<RecommendationResponseShort>> getRecommendationsByAnime(@PathVariable Long animeId) {
        List<RecommendationResponseShort> recommendations = recommendationService.getRecommendationsByAnime(animeId);
        return ResponseEntity.ok(recommendations);
    }

    @Operation(
            summary = "Add recommendation for an anime",
            description = "Create a recommendation associated to authenticated user."
    )
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RecommendationResponse> addRecommendation(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid RecommendationRequest request) {
        RecommendationResponse created = recommendationService.addRecommendation(userDetail.getId(), request);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Update existing recommendation",
            description = "Update a recommendation given its ID, only if it belongs to the authenticated user."
    )
    @PutMapping("/{recommendationId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RecommendationResponse> updateRecommendation(
            @AuthenticationPrincipal CustomUserDetail UserDetail,
            @PathVariable Long recommendationId,
            @RequestBody @Valid RecommendationRequest request) {
        RecommendationResponse update = recommendationService.updateRecommendation(recommendationId, request, userDetail.getUser());
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a recommendation",
            description = "Delete a recommendation given its ID, only if it belongs to the authenticated user."
    )
    @DeleteMapping("/{recommendationId}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteRecommendation(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long recommendationId) {
        String message = recommendationService.deleteRecommendation(recommendationId, userDetail.getUser());
        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Get recommendations made by the authenticated user.",
            description = "Retrieves the list of recommendations created by the current user."
    )
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<RecommendationResponse>> getMyRecommendations (
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        List<RecommendationResponse> list = recommendationService.getRecommendationByUser(userDetail.getUser());
        return ResponseEntity.ok(list);
    }



}
