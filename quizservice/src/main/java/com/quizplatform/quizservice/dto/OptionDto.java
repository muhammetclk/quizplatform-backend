package com.quizplatform.quizservice.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OptionDto {
    private UUID id;
    private String content;
    private Integer orderIndex;
    // isCorrect frontend'e gönderilmez! Sadece sonuç ekranında gelir
}
