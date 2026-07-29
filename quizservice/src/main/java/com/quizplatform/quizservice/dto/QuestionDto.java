package com.quizplatform.quizservice.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class QuestionDto {
    private UUID id;
    private String content;
    private String imageUrl;
    private String difficulty;
    private Integer orderIndex;
    private List<OptionDto> options;
}