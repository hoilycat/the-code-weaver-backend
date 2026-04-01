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

    // 1. 모든 프로젝트 목록 가져오기 (최신순)
    @GetMapping
    public List<Project> getProjects(){
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // 2. 개별 프로젝트 상세 조회
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 프로젝트를 찾을 수 없어요"));
    }

    // 3. 새 프로젝트 등록
    @PostMapping
    public Project createProject(@RequestBody Project project){
        return projectRepository.save(project);
    }

    // 4. 프로젝트 수정
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project newProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setTitle(newProject.getTitle());
                    project.setCategory(newProject.getCategory());
                    project.setDescription(newProject.getDescription());
                    project.setPeriod(newProject.getPeriod());
                    project.setLink(newProject.getLink());
                    project.setSize(newProject.getSize());
                    project.setStatus(newProject.getStatus());
                    project.setImages(newProject.getImages()); // 사진 리스트 업데이트
                    project.setSnapshot(newProject.getSnapshot()); // 대표 사진 업데이트
                    return projectRepository.save(project);
                }).orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));
    }

    // 5. 프로젝트 삭제
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    // 6. 여러 장 업로드 API
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