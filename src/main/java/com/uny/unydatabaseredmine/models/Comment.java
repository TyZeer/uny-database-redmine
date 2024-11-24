package com.uny.unydatabaseredmine.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Comment {
    @Id
    private Long id;
    private Long taskId;
    private Long employeeId;
    private String commentText;
    private Timestamp createdAt;
}
