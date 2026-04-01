package com.weaver.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Value("${admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String inputPassword = credentials.get("password");
        if (adminPassword.equals(inputPassword)) {
            return ResponseEntity.ok(Map.of("token", "secret-key-12345"));
        }
        return ResponseEntity.status(401).body("비밀번호가 틀렸습니다.");
    }
}