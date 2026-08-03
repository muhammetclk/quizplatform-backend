package com.quizplatform.quizservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {

    @NotBlank(message = "Soru içeriği boş olamaz")
    private String content;

    private String imageUrl;
    private String difficulty = "MEDIUM";
    private String explanation;
    private Integer orderIndex;

    @Valid
    @NotEmpty(message = "En az bir seçenek olmalıdır")
    private List<CreateOptionRequest> options;
}
