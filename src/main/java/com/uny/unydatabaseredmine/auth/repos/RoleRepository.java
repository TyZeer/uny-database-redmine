package com.uny.unydatabaseredmine.auth.repos;


import com.uny.unydatabaseredmine.auth.models.Role;
import com.uny.unydatabaseredmine.auth.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}

