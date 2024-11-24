package com.uny.unydatabaseredmine.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    private Long id;
    private Long projectId;
    private String title;
    @Enumerated(EnumType.STRING)
    private Categories category;
    @Column(name = "due_date")
    private Date dueDate;
    @Column(name = "total_time_spent")
    private Integer totalTimeSpent;
    private int isCompleted;



}
