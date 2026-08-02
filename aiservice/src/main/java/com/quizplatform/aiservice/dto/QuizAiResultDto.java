package com.quizplatform.aiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizAiResultDto {
    private UUID quizId;
    private UUID attemptId;
    private BigDecimal score;
    private List<ExplainedAnswer> explanations;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExplainedAnswer {
        private UUID questionId;
        private String questionContent;
        private String selectedOptionContent;
        private String aiExplanation;
    }
}
