package com.quizplatform.quizservice.controller;

import com.quizplatform.quizservice.dto.AttemptResultDto;
import com.quizplatform.quizservice.dto.SubmitAttemptRequest;
import com.quizplatform.quizservice.exception.ApiResponse;
import com.quizplatform.quizservice.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/attempt")
    public ResponseEntity<ApiResponse<AttemptResultDto>> submitAttempt(
            @RequestHeader(value = "X-User-Id") String userIdHeader,
            @RequestBody SubmitAttemptRequest request) {

        UUID userId = UUID.fromString(userIdHeader);
        AttemptResultDto result = attemptService.submitAttempt(userId, request);
        return ResponseEntity.ok(ApiResponse.success(result, "Quiz başarıyla gönderildi ve değerlendirildi."));
    }
}
