package org.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.entity.GameEntity;
import org.entity.PlayerEntity;
import org.entity.ResultEntity;
import org.util.JpaLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultRepository extends AbstractRepository<ResultEntity, Long> {
    
    @Override
    public ResultEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ResultEntity.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Dynamic Search using Criteria API (Advanced Requirement)
     * Finds results matching multiple optional filters.
     */
    public List<ResultEntity> findResultsWithFilters(
            String playerNamePrefix,
            Integer minScore,
            Boolean isBot,
            LocalDateTime gameStartTimeAfter) {
        
        EntityManager em = getEntityManager();
        long start = System.nanoTime();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ResultEntity> cq = cb.createQuery(ResultEntity.class);
            Root<ResultEntity> result = cq.from(ResultEntity.class);
            
            // Joins to access Player and Game properties
            Join<ResultEntity, PlayerEntity> playerJoin = result.join("player");
            Join<ResultEntity, GameEntity> gameJoin = result.join("game");
            
            List<Predicate> predicates = new ArrayList<>();
            
            // Filter 1: Player name starts with (prefix)
            if (playerNamePrefix != null && !playerNamePrefix.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(playerJoin.get("name")), playerNamePrefix.trim().toLowerCase() + "%"));
            }
            
            // Filter 2: Score >= minScore
            if (minScore != null) {
                predicates.add(cb.ge(result.get("score"), minScore));
            }
            
            // Filter 3: Player is bot or not
            if (isBot != null) {
                predicates.add(cb.equal(playerJoin.get("isBot"), isBot));
            }
            
            // Filter 4: Game start time is after gameStartTimeAfter
            if (gameStartTimeAfter != null) {
                predicates.add(cb.greaterThan(gameJoin.get("startTime"), gameStartTimeAfter));
            }
            
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            
            // Order by score descending
            cq.orderBy(cb.desc(result.get("score")));
            
            List<ResultEntity> results = em.createQuery(cq).getResultList();
            long elapsedUs = (System.nanoTime() - start) / 1000;
            JpaLogger.info("Criteria Dynamic Search executed in " + elapsedUs + " us. Found " + results.size() + " matches.");
            return results;
        } catch (Exception e) {
            JpaLogger.error("Exception executing Criteria Dynamic Search", e);
            throw e;
        } finally {
            em.close();
        }
    }
}
