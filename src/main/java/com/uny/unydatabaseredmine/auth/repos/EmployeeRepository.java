package com.uny.unydatabaseredmine.auth.repos;

import com.uny.unydatabaseredmine.auth.models.Employee;
import com.uny.unydatabaseredmine.auth.models.Role;
import com.uny.unydatabaseredmine.auth.models.RoleName;
import com.uny.unydatabaseredmine.models.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }
    public Boolean existsByEmail(String email){
        String sql = "select count(*) from employees where email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Optional<Employee> findByEmail(String email) {
        String emailString = String.format("'%s'", email);
        String sql = "SELECT * FROM employees WHERE email = " + emailString;
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setName(rs.getString("name"));
                employee.setJob_title(rs.getString("job_title"));
                employee.setEmail(rs.getString("email"));
                employee.setPassword(rs.getString("password"));
                employee.setRoles(getRolesForEmployee(employee.getId()));
                return Optional.of(employee);
            }
            return Optional.empty();
        });
    }

    public Optional<Employee> findByEmailWithoutRole(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setName(rs.getString("name"));
                employee.setJob_title(rs.getString("job_title"));
                employee.setEmail(rs.getString("email"));
                employee.setPassword(rs.getString("password"));
                return Optional.of(employee);
            }
            return Optional.empty();
        }, email);
    }

    // Alias method for findByEmail
    public Optional<Employee> findUserByEmail(String username) {
        return findByEmail(username);
    }
    public Boolean  existsByUsername(String username) {
        String sql = "select count(*) from employees where email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    // Fetch roles for a given employee
    private List<Role> getRolesForEmployee(Long employeeId) {
        String sql = "SELECT r.id, r.role_name FROM roles r " +
                "JOIN employee_roles er ON r.id = er.role_id " +
                "WHERE er.employee_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Role role = new Role();
            role.setId(rs.getLong("id"));
            role.setRoleName(RoleName.valueOf(rs.getString("role_name")));
            return role;
        }, employeeId);
    }

    // Save or update an employee

    public Employee save(Employee employee) {
        transactionTemplate.execute((TransactionStatus ts) ->{
            String sql = "INSERT INTO employees (name, email, password, job_title) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE name = VALUES(name), password = VALUES(password)";
            jdbcTemplate.update(sql,
                    employee.getName(),
                    employee.getEmail(),
                    employee.getPassword(),
                    employee.getJob_title());
            Optional<Employee> possibleSave = findByEmailWithoutRole(employee.getEmail());
            if(possibleSave.isPresent()) {
                if (employee.getRoles() != null) {
                    for (Role role : employee.getRoles()) {
                        saveEmployeeRole(possibleSave.get().getId(), role.getId());
                    }
                }
            }
            return employee;
        });
        return employee;
    }

    // Save a role assignment for an employee
    private void saveEmployeeRole(Long employeeId, Long roleId) {
        String sql = "INSERT INTO employee_roles (employee_id, role_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, employeeId, roleId);
    }
    public String findEmployeeById(Long employeeId) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        Employee employee = jdbcTemplate.queryForObject(sql, this::mapRowToEmployee,employeeId);
        assert employee != null;
        return employee.getName();
    }
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql,this::mapRowToEmployee);

    }
    private Employee mapRowToEmployee(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("job_title"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}

