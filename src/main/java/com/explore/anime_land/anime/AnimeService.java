package com.explore.anime_land.anime;

import com.explore.anime_land.anime.dto.AnimeMapper;
import com.explore.anime_land.anime.dto.AnimeRequest;
import com.explore.anime_land.anime.dto.AnimeResponse;
import com.explore.anime_land.anime.dto.AnimeResponseShort;
import com.explore.anime_land.exceptions.EntityNotFoundException;
import com.explore.anime_land.users.Role;
import com.explore.anime_land.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import com.explore.anime_land.users.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final UserService userService;

    public List<AnimeResponseShort> getAllAnimes(){
        List<Anime> animes = animeRepository.findAll();
        return listToDtoShort(animes);
    }

    public List<AnimeResponseShort> getAllAnimesUser(String username){
        List<Anime> animes = animeRepository.findAll();

        animes.sort(Comparator.comparing(anime -> anime.getUser() != null && username.equals(anime.getUser().getUsername()) ? 0 : 1));
        return listToDtoShort(animes);
    }

    public List<AnimeResponse> getFilteredAnimes(String genre, String title) {
        List<Anime> filtered = new ArrayList<>();
        boolean genreIsEmpty = genre == null || genre.isBlank();
        boolean titleIsEmpty = title == null || title.isBlank();

        if (!genreIsEmpty && !titleIsEmpty) {
            filtered = animeRepository.findByGenreContainingIgnoreCaseAndTitleContainingIgnoreCase(genre, title);
        } else if (!genreIsEmpty) {
            filtered = animeRepository.findByGenreContainingIgnoreCase(genre);
        } else if (!titleIsEmpty) {
            filtered = animeRepository.findByTitleContainingIgnoreCase(title);}
        return listToDto(filtered);
    }

    public AnimeResponse getAnimeById(Long animeId){
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(()-> new EntityNotFoundException("Anime", "id", animeId.toString()));
        return AnimeMapper.toDto(anime);
    }

    public List<AnimeResponse> getAnimesByUsername(String username){
        User user = userService.getByUsername(username);
        List<Anime> listToDto = animeRepository.findByUser(user);
        return listToDto(listToDto);
    }

    @PreAuthorize("isAuthenticated()")
    public AnimeResponse addAnime(AnimeRequest animeRequest, User user) {
        Anime anime = AnimeMapper.toEntity(animeRequest, user);
        Anime savedAnime = animeRepository.save(anime);
        return AnimeMapper.toDto(savedAnime);
    }

    @PreAuthorize("isAuthenticated()")
    public AnimeResponse updateAnime(Long animeId, AnimeRequest request, User user) {
        Anime anime = findAnimeOrThrow(animeId);
        assertUserIsOwner(anime, user);

        anime.setTitle(request.title());
        anime.setGenre(request.genre());
        anime.setDescription(request.description());
        anime.setImageUrl(request.imageUrl());

        Anime updated = animeRepository.save(anime);
        return AnimeMapper.toDto(updated);
    }

    @PreAuthorize("isAuthenticated()")
    public String deleteAnime(Long id, User user) {
        Anime anime = findAnimeOrThrow(id);
        assertUserIsOwnerOrAdmin(anime, user);
        animeRepository.delete(anime);
        return "Anime with id " + id + " deleted successfully";
    }

    private Anime findAnimeOrThrow(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Anime", "id", id.toString()));
    }

    public void assertUserIsOwner(Anime anime, User user) {
        if (!anime.getUser().getId().equals(user.getId()) ) {
            throw new AccessDeniedException("You are not authorized to modify or delete this anime.");
        }
    }

    private void assertUserIsOwnerOrAdmin(Anime anime, User user) {
        if (!anime.getUser().getId().equals(user.getId()) && !user.hasRole(Role.ADMIN)) {
            throw new AccessDeniedException("You are not authorized to modify or delete this anime.");
        }
    }

    private List<AnimeResponse> listToDto (List<Anime> animes){
        return animes.stream()
                .map(AnimeMapper::toDto)
                .toList();
    }

    private List<AnimeResponseShort> listToDtoShort (List<Anime> animes){
        return animes.stream()
                .map(AnimeMapper::toDtoShort)
                .toList();
    }

}
