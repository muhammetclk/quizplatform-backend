package com.quizplatform.quizservice.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class QuizDetailDto {
    private UUID id;
    private String title;
    private String difficulty;
    private Integer timeLimitSec;
    private List<QuestionDto> questions;
}
