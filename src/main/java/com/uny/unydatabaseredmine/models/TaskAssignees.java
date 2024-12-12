package com.uny.unydatabaseredmine.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskAssignees {
    @Id
    private Long taskId;

    private Long employeeId;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    public TaskAssignees(long taskId, long employeeId, Severity severity) {
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.severity = severity;
    }
}
