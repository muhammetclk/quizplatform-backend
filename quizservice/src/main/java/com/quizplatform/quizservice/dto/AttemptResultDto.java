package com.quizplatform.quizservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AttemptResultDto {
    private UUID attemptId;
    private String quizTitle;
    private BigDecimal score;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer emptyCount;
    private Integer totalQuestions;
    private Integer timeSpentSec;
    private List<AnswerResultDto> answers;

    @Data @Builder
    public static class AnswerResultDto {
        private UUID questionId;
        private String questionContent;
        private UUID selectedOptionId;
        private UUID correctOptionId;
        private boolean isCorrect;
        private String explanation;      // statik açıklama
        private String aiExplanation;    // AI açıklama (cache'deyse gelir)
    }
}
