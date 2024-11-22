package com.proj.recipe.repos;

import com.proj.recipe.models.Role;
import com.proj.recipe.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}

