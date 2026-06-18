package com.quizplatform.quizservice.dto;

import java.util.UUID;

import com.quizplatform.quizservice.entity.Topic.DifficultyLevel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDto {

    
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Integer orderIndex;
    private DifficultyLevel difficultyLevel;

    

    

    
    
}
