package com.quizplatform.quizservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class QuizSummaryDto {
    private UUID id;
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimitSec;
    private Integer questionCount;
    private LocalDateTime createdAt;
}
