package com.explore.anime_land.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import com.explore.anime_land.users.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByGenreContainingIgnoreCase(String genre);
    List<Anime> findByUser(User user);
    List<Anime> findByTitleContainingIgnoreCase(String title);
    List<Anime> findByGenreContainingIgnoreCaseAndTitleContainingIgnoreCase(String genre, String title);
}
