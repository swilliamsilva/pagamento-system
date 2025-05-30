package com.pagamento.common.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    private UUID id;
    private String entityName;
    private String entityId;
    private String action; // CREATE, UPDATE, DELETE
    private String details;
    private String performedBy;
    private Date performedAt;

    public AuditLog() {
        this.performedAt = new Date();
    }

    public AuditLog(String entityName, String entityId, String action, 
                   String details, String performedBy) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.performedAt = new Date();
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public Date getPerformedAt() { return performedAt; }
    public void setPerformedAt(Date performedAt) { this.performedAt = performedAt; }
}
