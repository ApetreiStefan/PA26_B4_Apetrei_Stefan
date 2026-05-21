package org.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@NamedQueries({
        @NamedQuery(name = "Player.findByName", query = "SELECT p FROM PlayerEntity p WHERE p.name = :name"),
        @NamedQuery(name = "Player.findAll", query = "SELECT p FROM PlayerEntity p")
})
@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(AuditListener.class)
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "is_bot")
    private boolean isBot;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<ResultEntity> results = new ArrayList<>();

    public PlayerEntity(String name, boolean isBot) {
        this.name = name;
        this.isBot = isBot;
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isBot=" + isBot +
                '}';
    }
}
