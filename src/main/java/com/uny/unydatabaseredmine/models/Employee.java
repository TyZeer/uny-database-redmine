package com.uny.unydatabaseredmine.models;

import jakarta.persistence.*;

@Table(name = "employees")
@Entity
public class Employee {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EmployeesRoles role;
}
