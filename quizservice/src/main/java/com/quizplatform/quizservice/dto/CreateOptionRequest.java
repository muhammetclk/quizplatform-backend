package com.quizplatform.quizservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOptionRequest {

    @NotBlank(message = "Seçenek içeriği boş olamaz")
    private String content;

    @NotNull(message = "Doğru seçenek işareti zorunludur")
    private Boolean isCorrect;

    private Integer orderIndex;
}
