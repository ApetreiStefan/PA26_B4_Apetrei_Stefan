package org.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "action_type", nullable = false) // INSERT, UPDATE, DELETE
    private String actionType;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "details", length = 2000)
    private String details;

    public AuditLogEntity(String entityName, Long entityId, String actionType, String details) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.actionType = actionType;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }
}
