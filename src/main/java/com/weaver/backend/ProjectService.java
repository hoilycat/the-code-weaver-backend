package com.weaver.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found."));
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project newProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setTitle(newProject.getTitle());
                    project.setCategory(newProject.getCategory());
                    project.setDescription(newProject.getDescription());
                    project.setPeriod(newProject.getPeriod());
                    project.setLink(newProject.getLink());
                    project.setSize(newProject.getSize());
                    project.setStatus(newProject.getStatus());
                    project.setImages(newProject.getImages());
                    project.setSnapshot(newProject.getSnapshot());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("Project not found."));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public long countProjects() {
        return projectRepository.count();
    }
}
