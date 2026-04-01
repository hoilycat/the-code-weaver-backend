package com.weaver.backend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

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
    private String snapshot; // 대표 이미지 경로
    private String link;
    private String period;

    @ElementCollection
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_path")
    private List<String> images = new ArrayList<>();
}