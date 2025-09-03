package com.explore.anime_land.recommendation;

import com.explore.anime_land.anime.Anime;
import com.explore.anime_land.users.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    @ManyToOne
     @JoinColumn(name ="anime_id")
     private Anime anime;

    @ManyToOne
     @JoinColumn(name ="user_id")
     private User author;
}
