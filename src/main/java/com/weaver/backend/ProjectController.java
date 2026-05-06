package com.weaver.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = {"http://localhost:5173", "https://the-code-weaver-frontend.vercel.app"}) // 배포 주소도 추가
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

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

    // 6. 여러 장 업로드 API (Supabase Storage 연동)
    @PostMapping("/upload-multiple")
    public List<String> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> publicUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                
                // Supabase Storage 업로드 URL
                String uploadUrl = String.format("%s/storage/v1/object/%s/%s", supabaseUrl, supabaseBucket, fileName);

                // 헤더 설정
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + supabaseKey);
                headers.set("apikey", supabaseKey);
                headers.setContentType(MediaType.parseMediaType(file.getContentType()));

                HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

                try {
                    // 1. Supabase에 파일 업로드 (POST)
                    restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);
                    
                    // 2. 업로드 성공 시 Public URL 생성
                    String publicUrl = String.format("%s/storage/v1/object/public/%s/%s", supabaseUrl, supabaseBucket, fileName);
                    publicUrls.add(publicUrl);
                } catch (Exception e) {
                    System.err.println("Upload failed for " + fileName + ": " + e.getMessage());
                }
            }
        }
        return publicUrls;
    }
}