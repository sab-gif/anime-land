package com.explore.anime_land.users;


import com.explore.anime_land.anime.Anime;
import jakarta.persistence.*;
import lombok.*;



import java.util.List;

@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "anime",cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Anime> anime;

    public boolean hasRole(Role role) { return this.role == role; }
}
