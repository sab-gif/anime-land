package com.explore.anime_land.recommendation;

import com.explore.anime_land.anime.Anime;
import com.explore.anime_land.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
     @JoinColumn(name ="anime_id")
     private Anime anime;

    @ManyToOne
     @JoinColumn(name ="user_id")
     private User user;

    @Column(name = "date_posted", nullable = false)
    private LocalDateTime datePosted;
}
