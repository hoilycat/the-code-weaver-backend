package com.weaver.backend;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getProjects(){
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 프로젝트를 찾을 수 없어요"));
    }

    @PostMapping
    public Project createProject(@RequestBody Project project){
        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    @PostMapping("/upload-multiple")
    public List<String> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> filePaths = new ArrayList<>();
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadDir + fileName));
                filePaths.add("/uploads/" + fileName);
            }
        }
        return filePaths;
    }
}