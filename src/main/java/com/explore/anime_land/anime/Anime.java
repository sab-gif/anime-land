package com.explore.anime_land.anime;

import com.explore.anime_land.recommendation.Recommendation;
import com.explore.anime_land.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name= "animes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false, length = 1024)
    private String description;

    @Column(name = "image_Url")
    private String imageUrl;

    @Column(name = "average_rating")
    private double averageRating;

    @ManyToOne
            @JoinColumn(name = "user_id")
            private User user;

            @OneToMany(mappedBy = "anime",cascade = CascadeType.ALL,orphanRemoval = true)
            private List<Recommendation> recommendations;
}
