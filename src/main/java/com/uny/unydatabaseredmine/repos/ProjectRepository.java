package com.uny.unydatabaseredmine.repos;

import com.uny.unydatabaseredmine.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Project> findAll() {
        String sql = "select * from projects";
        return jdbcTemplate.query(sql, this::mapRowToProject);
    }
    public List<Project> findByName(String name) {
        String sql = "SELECT * FROM projects WHERE name LIKE ?";
        return jdbcTemplate.query(sql, this::mapRowToProject, "%" + name + "%");
    }

    public void addProject(String name, String description, Date startDate, Date endDate) {
        jdbcTemplate.update("CALL add_project(?, ?, ?, ?)",
                name,
                description,
                new java.sql.Date(startDate.getTime()),
                new java.sql.Date(endDate.getTime())
        );
    }
    public Project findById(Long id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        return jdbcTemplate.query(sql, this::mapRowToProject, id).get(0);
    }

    private Project mapRowToProject(ResultSet rs, int rowNum) throws SQLException {
        return new Project(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("start_date"),
                rs.getDate("end_date")
        );
    }

    public void updateTask(Project project) {
        jdbcTemplate.update("CALL update_project(?, ?, ?, ?, ?)",
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    public void deleteTask(Long id) {
        jdbcTemplate.update("CALL delete_project(?)", id);
    }

}
