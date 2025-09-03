package com.explore.anime_land.anime;

import com.explore.anime_land.recommendation.Recommendation;
import com.explore.anime_land.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name= "anime")
@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    private Long id;
    @ManyToOne
            @JoinColumn(name = "user_id")
            private User user;

            @OneToMany(mappedBy = "anime",cascade = CascadeType.ALL,orphanRemoval = true)
            private List<Recommendation> recommendations;
}
