# ⚙️ Backend: The Weaver Content Management API

> **"Storing the threads of creativity."**  
> 포트폴리오의 영속성을 위해 전시물 데이터를 안전하게 저장하고, 대용량 이미지 자산을 효율적으로 관리하는 Spring Boot 기반의 REST API 서버입니다.

이 서버는 'The Weaver' 프로젝트의 핵심 데이터를 관리하며, 관리자가 직접 포트폴리오를 업데이트할 수 있는 CMS(Content Management System) 기능을 제공합니다.

---

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.4.x
- **Language**: Java 17
- **Database**: MySQL (Production), H2 (Development/Test)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Lombok**: Boilerplate 코드 최적화

---

## 🏗️ Backend Architecture & Deep-Dive

### 1. 효율적인 데이터 구조 설계 (JPA Entity)
매거진 스타일의 포트폴리오는 게시물 하나당 다수의 이미지와 긴 본문을 포함합니다. 이를 처리하기 위해 다음과 같은 전략을 사용했습니다.

- **`@ElementCollection` 활용**: 이미지 경로 리스트(`List<String> images`)를 별도의 엔티티 선언 없이도 1:N 관계의 테이블로 자동 매핑하여 데이터 구조를 단순화했습니다.
- **Large Text Handling**: 프로젝트 스토리텔링을 위해 `description` 필드에 `@Column(columnDefinition = "TEXT")`를 적용하여 데이터 길이 제한 문제를 해결했습니다.

**[Project.java]**
```java
@Entity
@Getter @Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description; // 긴 문단을 저장하기 위한 설정

    @ElementCollection // 다중 이미지 경로를 저장하는 효율적인 방법
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_path")
    private List<String> images = new ArrayList<>();

    private String snapshot; // 대표 썸네일 경로
}
```
---

##  2. 다중 파일 업로드 및 서버 리소스 매핑

포트폴리오의 시각적 자산을 관리하기 위해 안정적인 파일 저장 로직을 구축했습니다.
* **파일명 중복 방지**: System.currentTimeMillis()를 원본 파일명과 결합하여 서버 내 파일명 충돌을 방지했습니다.
* **물리적 경로 매핑**: WebMvcConfigurer를 통해 서버 로컬 폴더(/uploads/)를 외부에서 URL로 접근 가능하도록 정적 리소스 핸들러를 설정했습니다.
<br><br>

**[ProjectController.java]**
```
Java
@PostMapping("/upload-multiple")
public List<String> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
    List<String> filePaths = new ArrayList<>();
    String uploadDir = System.getProperty("user.dir") + "/uploads/"; // 프로젝트 루트의 uploads 폴더
    
    for (MultipartFile file : files) {
        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + fileName));
            filePaths.add("/uploads/" + fileName); // DB에 저장할 상대 경로 반환
        }
    }
    return filePaths;
}
```
---

## 3. 보안 및 관리자 인증

Environment Variables: 관리자 비밀번호를 application.properties의 ${MY_ADMIN_PASSWORD} 형식으로 관리하여 소스 코드 유출 시에도 보안을 유지하도록 설계했습니다.
CORS Configuration: 프론트엔드(localhost:5173)와의 안전한 데이터 통신을 위해 CORS 정책을 명시적으로 허용했습니다.

## 🚀 API Endpoints
### Projects

**Method** |	**Endpoint** |	**Description**
| --- | --- | --- |
**GET**	 |	**/api/projects**	 |	모든 전시물 목록 조회 (최신순)
**GET** |		**/api/projects/{id}**	 |	특정 전시물 상세 정보 조회
**POST** |		**/api/projects/upload-multiple** |		다중 이미지 파일 서버 업로드
**POST** |		**/api/projects**	 |	새로운 전시물 게시글 등록
**PUT** |		**/api/projects/{id}**	 |	기존 전시물 정보 수정
**DELETE** |		**/api/projects/{id}** |		전시물 삭제


### Authentication
**Method** |		**Endpoint** |		**Description**
| --- | --- | --- |
**POST** |		**/api/auth/login**	 |	관리자 로그인 및 인증

### 🔧 Database Schema (ERD)
(여기에 ERD 이미지 링크 또는 캡처본을 삽입)
<br><br><br><br>

### 🔗 Frontend Repository 바로가기
[ The Weaver Frontend Repository ](https://github.com/hoilycat/the-code-weaver-frontend)
