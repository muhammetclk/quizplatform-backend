package com.quizplatform.quizservice.dto;

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
public class QuizAttemptEventDto {
    private UUID userId;
    private UUID quizId;
    private UUID attemptId;
    private BigDecimal score;
    private List<WrongAnswerDto> wrongAnswers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WrongAnswerDto {
        private UUID questionId;
        private String questionContent;
        private String selectedOptionContent;
        private String correctOptionContent;
    }
}
