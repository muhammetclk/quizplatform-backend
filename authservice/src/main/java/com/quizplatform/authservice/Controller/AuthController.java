package com.quizplatform.authservice.Controller;


import com.quizplatform.authservice.dto.*;
import com.quizplatform.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Başarıyla çıkış yapıldı");
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(@RequestHeader("Authorization") String authHeader) {
        // Sadece token geçerliyse buraya ulaşılır (JwtAuthFilter sayesinde)
        return ResponseEntity.ok("Token geçerli");
    }
}