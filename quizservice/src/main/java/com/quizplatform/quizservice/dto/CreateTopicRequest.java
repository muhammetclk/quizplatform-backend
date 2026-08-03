package com.quizplatform.quizservice.dto;

import com.quizplatform.quizservice.entity.Topic.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateTopicRequest {

    @NotNull(message = "Kategori ID zorunludur")
    private UUID categoryId;

    @NotBlank(message = "Konu adı boş olamaz")
    private String name;

    /** If blank, will be auto-generated from name. */
    private String slug;

    private String description;

    private DifficultyLevel difficultyLevel = DifficultyLevel.BEGINNER;

    private Integer orderIndex;
}
