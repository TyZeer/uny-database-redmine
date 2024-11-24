package com.uny.unydatabaseredmine.auth.repos;


import com.uny.unydatabaseredmine.auth.models.Role;
import com.uny.unydatabaseredmine.auth.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find a Role by RoleName
    public Optional<Role> findByRoleName(RoleName roleName) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setRoleName(RoleName.valueOf(rs.getString("role_name")));
                return Optional.of(role);
            }
            return Optional.empty();
        }, roleName.name());
    }

    // Save a new Role
    public Role save(Role role) {
        String sql = "INSERT INTO roles (role_name) VALUES (?)";
        jdbcTemplate.update(sql, role.getRoleName().name());
        return role;
    }
}

