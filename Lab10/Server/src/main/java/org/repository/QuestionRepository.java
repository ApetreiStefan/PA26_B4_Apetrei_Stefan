package org.repository;

import jakarta.persistence.EntityManager;
import org.entity.QuestionEntity;
import java.util.List;

public class QuestionRepository extends AbstractRepository<QuestionEntity, Long> {

    @Override
    public QuestionEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(QuestionEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<QuestionEntity> findAll() {
        return runReadQuery("SELECT q FROM QuestionEntity q", QuestionEntity.class, null, true);
    }
}
