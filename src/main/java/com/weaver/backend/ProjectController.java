package com.weaver.backend;

//import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")

public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    //모든 프로젝트 목록 가져오기(전체보기)
    @GetMapping
    public List<Project> getProjects(){
            return projectRepository.findAll();
    }

    //개별 프로젝트 상세 조회( 상세 페이지용)
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 프로젝트를 찾을 수 없어요"));
    }

}
