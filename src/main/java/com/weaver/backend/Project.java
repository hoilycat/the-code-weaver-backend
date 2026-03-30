package com.weaver.backend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String size;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;
    private String snapshot;
    private String link;
}
