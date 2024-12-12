package com.uny.unydatabaseredmine.repos;

import com.uny.unydatabaseredmine.models.Categories;
import com.uny.unydatabaseredmine.models.Project;
import com.uny.unydatabaseredmine.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class TaskRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, this::mapRowToTask);
    }

    public List<Task> findByProjectId(Long projectId) {
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToTask, projectId);
    }

    public List<Task> findByCategory(String category) {
        String sql = "SELECT * FROM tasks WHERE category = ?";
        return jdbcTemplate.query(sql, this::mapRowToTask, category);
    }

    public void addTask(Long projectId, String title, String category, Date dueDate, int totalTimeSpent, boolean isCompleted) {
        int isCmp;
        if (isCompleted)
            isCmp = 1;
        else
            isCmp = 0;
        String sql = "INSERT INTO tasks (project_id, title, category, due_date, total_time_spent, is_completed) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, projectId, title, category, new java.sql.Date(dueDate.getTime()), totalTimeSpent, isCmp);
    }
    public Task findById(Long id) {

        String sql = "SELECT * FROM tasks WHERE id = ? LIMIT 1";
        return jdbcTemplate.query(sql, this::mapRowToTask, id).get(0);

    }

    private Task mapRowToTask(ResultSet rs, int rowNum) throws SQLException {
        boolean isCompleted;
        if(rs.getInt("is_completed") > 0){
            isCompleted = true;
        }
        else
            isCompleted = false;
        return new Task(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getString("title"),
                Categories.valueOf(rs.getString("category")),
                rs.getDate("due_date"),
                rs.getInt("total_time_spent"),
                isCompleted
        );
    }

    public void updateTask(Task task) {
        int completed = task.isCompleted() ? 1 : 0;
        jdbcTemplate.update("CALL update_task(?, ?, ?, ?, ?, ?)",
                task.getId(),
                task.getTitle(),
                task.getCategory().name(),
                task.getDueDate(),
                task.getTotalTimeSpent(),
                completed
        );
    }

    public void deleteTask(Long id) {
        jdbcTemplate.update("call delete_task(?)", id);
    }

    public void assignTaskToProject(Long projectId, Long taskId) {
        String sql = "UPDATE tasks SET project_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, projectId, taskId);
    }
}