package org.repository;

import jakarta.persistence.EntityManager;
import org.entity.PlayerEntity;
import java.util.List;
import java.util.Map;

public class PlayerRepository extends AbstractRepository<PlayerEntity, Long> {
    
    @Override
    public PlayerEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlayerEntity.class, id);
        } finally {
            em.close();
        }
    }

    public PlayerEntity findByName(String name) {
        List<PlayerEntity> players = runReadQuery(
            "SELECT p FROM PlayerEntity p WHERE p.name = :name",
            PlayerEntity.class,
            Map.of("name", name),
            true
        );
        return players.isEmpty() ? null : players.get(0);
    }

    public List<PlayerEntity> findAll() {
        return runReadQuery("SELECT p FROM PlayerEntity p", PlayerEntity.class, null, true);
    }

    // Specific JPQL Read Query (Homework Requirement)
    public List<PlayerEntity> findBots() {
        return runReadQuery("SELECT p FROM PlayerEntity p WHERE p.isBot = true", PlayerEntity.class, null, true);
    }

    // Transactional, Modifying JPQL Query (Homework Requirement)
    public int updateBotStatusByNamePattern(String pattern, boolean isBot) {
        return runModifyingQuery(
            "UPDATE PlayerEntity p SET p.isBot = :isBot WHERE p.name LIKE :pattern",
            Map.of("isBot", isBot, "pattern", pattern)
        );
    }
}
