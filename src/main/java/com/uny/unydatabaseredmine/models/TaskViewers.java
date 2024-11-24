package com.uny.unydatabaseredmine.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TaskViewers {
    @Id
    private Long taskId;

    private Long employeeId;
    @Enumerated(EnumType.STRING)
    private AccessLevels access_level;
}
