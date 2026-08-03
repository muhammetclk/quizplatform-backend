package com.quizplatform.quizservice.controller;

import com.quizplatform.quizservice.dto.*;
import com.quizplatform.quizservice.exception.ApiResponse;
import com.quizplatform.quizservice.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Admin-only REST controller for content management.
 * All endpoints require ROLE_ADMIN — enforced both at SecurityConfig
 * (path-level) and @PreAuthorize (method-level) for defence-in-depth.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // ── Category ─────────────────────────────────────────────────────────────

    @PostMapping("/category")
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {

        CategoryDto created = adminService.createCategory(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Kategori başarıyla oluşturuldu."));
    }

    // ── Topic ─────────────────────────────────────────────────────────────────

    @PostMapping("/topic")
    public ResponseEntity<ApiResponse<TopicDto>> createTopic(
            @Valid @RequestBody CreateTopicRequest request) {

        TopicDto created = adminService.createTopic(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Konu başarıyla oluşturuldu."));
    }

    // ── Quiz ──────────────────────────────────────────────────────────────────

    @PostMapping("/quiz")
    public ResponseEntity<ApiResponse<QuizSummaryDto>> createQuiz(
            @Valid @RequestBody CreateQuizRequest request,
            Authentication authentication) {

        // Extract userId from JWT principal (email is the principal in our filter)
        // createdBy is optional; pass null if not resolvable
        UUID createdBy = null;
        if (authentication != null) {
            try {
                createdBy = UUID.fromString(authentication.getName());
            } catch (IllegalArgumentException ignored) {
                // principal is email string, not UUID — that's fine
            }
        }

        QuizSummaryDto created = adminService.createQuiz(request, createdBy);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Quiz başarıyla oluşturuldu."));
    }
}
