package com.uny.unydatabaseredmine.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    private Long id;
    private String name;
    private String description;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
}