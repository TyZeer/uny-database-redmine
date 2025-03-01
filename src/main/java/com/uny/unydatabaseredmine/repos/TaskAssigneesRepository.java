package com.uny.unydatabaseredmine.repos;

import com.uny.unydatabaseredmine.models.Categories;
import com.uny.unydatabaseredmine.models.Severity;
import com.uny.unydatabaseredmine.models.Task;
import com.uny.unydatabaseredmine.models.TaskAssignees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

;import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskAssigneesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setAssignee(Long taskId, Long assigneeId, Severity severity) {
        String sql = "DELETE FROM task_assignees WHERE task_id=?";
        jdbcTemplate.update(sql, taskId);
        String severe  = String.valueOf(severity);
        sql = "INSERT INTO task_assignees (task_id, employee_id, severity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, taskId, assigneeId, severe);
        //INSERT INTO tasks (project_id, title, category, due_date, total_time_spent, is_completed) VALUES (?, ?, ?, ?, ?, ?)
    }
    public Long getAssignedEmployees(Long taskId) {
        String sql = "SELECT employee_id FROM task_assignees WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, taskId);
    }
    public List<Long> getAllTasksByAssignee(Long assignee) {
        String sql = "SELECT task_id FROM task_assignees WHERE employee_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{assignee}, Long.class);
    }

    private TaskAssignees mapRowToAssignee(ResultSet rs, int rowNum) throws SQLException {

        return new TaskAssignees(
                rs.getLong("task_id"),
                rs.getLong("employee_id"),
                Severity.valueOf(rs.getString("severity")));
    }
}
