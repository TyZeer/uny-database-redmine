package com.uny.unydatabaseredmine.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity


@Table(name = "tasks")
public class Task {
    @Id
    private Long id;
    private Long projectId;
    private String title;
    @Enumerated(EnumType.STRING)
    private Categories category;
    @Column(name = "due_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;
    @Column(name = "total_time_spent")
    private Integer totalTimeSpent;
    public Boolean isCompleted;

    public Task() {
    }
    public Task(Long id, Long projectId, String title, Categories category, Date dueDate, boolean isCompleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.category = category;
        this.dueDate = dueDate;
        this.totalTimeSpent = 0;
        this.isCompleted = isCompleted;
    }

    public Task(Long id, Long projectId, String title, Categories category, Date dueDate, Integer totalTimeSpent, boolean isCompleted) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.category = category;
        this.dueDate = dueDate;
        this.totalTimeSpent = totalTimeSpent;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(Integer totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public Boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }
}
