package org.repository;

import jakarta.persistence.EntityManager;
import org.entity.AuditLogEntity;

public class AuditLogRepository extends AbstractRepository<AuditLogEntity, Long> {

    @Override
    public AuditLogEntity findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AuditLogEntity.class, id);
        } finally {
            em.close();
        }
    }

    public static void log(String entityName, Long entityId, String actionType, String details) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Truncate details if they are too long for database column (length = 2000)
            if (details != null && details.length() > 1990) {
                details = details.substring(0, 1990) + "...";
            }
            AuditLogEntity logEntry = new AuditLogEntity(entityName, entityId, actionType, details);
            em.persist(logEntry);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Failed to persist audit log: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
