package com.uny.unydatabaseredmine.auth.config;

import com.uny.unydatabaseredmine.auth.models.Employee;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpSession;

public class RoleUtil {
    public static void extractAndSetUserRole(HttpSession session) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Extract role (e.g., ROLE_ADMIN -> admin)
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(auth -> auth.replace("ROLE_", ""))
                    .findFirst()
                    .orElse("user"); // Default role if none is found
            session.setAttribute("role", role);
        }
    }
    public static Long getUserId(HttpSession session) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Employee employee = (Employee) authentication.getPrincipal();
            return employee.getId();
        }
        return null;
    }
}
