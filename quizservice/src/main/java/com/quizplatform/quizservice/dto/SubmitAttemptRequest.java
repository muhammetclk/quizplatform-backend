package com.quizplatform.quizservice.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class SubmitAttemptRequest {
    private UUID quizId;
    private Integer timeSpentSec;
    private List<AnswerItem> answers;

    @Data
    public static class AnswerItem {
        private UUID questionId;
        private UUID selectedOptionId; // null = boş bırakıldı
    }
}
