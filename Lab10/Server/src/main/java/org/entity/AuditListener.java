package org.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.repository.AuditLogRepository;

public class AuditListener {

    @PostPersist
    public void onPostPersist(Object entity) {
        logAction(entity, "INSERT");
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        logAction(entity, "UPDATE");
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        logAction(entity, "DELETE");
    }

    private void logAction(Object entity, String action) {
        if (entity instanceof AuditLogEntity) {
            return;
        }

        String entityName = entity.getClass().getSimpleName();
        Long entityId = null;
        String details = entity.toString();

        try {
            java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
            entityId = (Long) getIdMethod.invoke(entity);
        } catch (Exception ignored) {}

        // Print to screen
        System.out.println("[AUDIT] DB change detected: " + action + " on " + entityName + " with ID " + entityId);
        
        // Persist to audit log database table
        AuditLogRepository.log(entityName, entityId, action, details);
    }
}
