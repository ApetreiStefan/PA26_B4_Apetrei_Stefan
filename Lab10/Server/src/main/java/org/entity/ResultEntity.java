package org.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(AuditListener.class)
public class ResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameEntity game;

    @Column(name = "score")
    private int score;

    @Column(name = "total_response_time")
    private long totalResponseTime;

    public ResultEntity(PlayerEntity player, GameEntity game, int score, long totalResponseTime) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.totalResponseTime = totalResponseTime;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "id=" + id +
                ", score=" + score +
                ", totalResponseTime=" + totalResponseTime +
                '}';
    }
}
