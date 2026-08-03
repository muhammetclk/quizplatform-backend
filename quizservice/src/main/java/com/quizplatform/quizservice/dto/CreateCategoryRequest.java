package com.quizplatform.quizservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Kategori adı boş olamaz")
    @Size(min = 2, max = 100)
    private String name;

    /** If blank, will be auto-generated from name in the service layer. */
    private String slug;

    private String iconUrl;
    private String colorHex;
    private Integer orderIndex;
}
