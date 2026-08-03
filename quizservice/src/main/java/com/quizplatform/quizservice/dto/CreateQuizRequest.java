package com.quizplatform.quizservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateQuizRequest {

    @NotNull(message = "Konu ID zorunludur")
    private UUID topicId;

    @NotBlank(message = "Quiz başlığı boş olamaz")
    private String title;

    private String description;

    private String difficulty = "MEDIUM";

    private Integer timeLimitSec = 600;

    private boolean isPublished = true;

    @Valid
    @NotEmpty(message = "En az bir soru olmalıdır")
    private List<CreateQuestionRequest> questions;
}
