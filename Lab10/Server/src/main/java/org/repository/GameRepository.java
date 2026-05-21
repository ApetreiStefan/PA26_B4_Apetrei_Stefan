package org.repository;

import jakarta.persistence.EntityManager;
import org.entity.GameEntity;
import java.util.List;

public class GameRepository extends AbstractRepository<GameEntity, Long> {
    @Override
    public GameEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GameEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<GameEntity> findAll() {
        return runReadQuery("SELECT g FROM GameEntity g", GameEntity.class, null, true);
    }
}
