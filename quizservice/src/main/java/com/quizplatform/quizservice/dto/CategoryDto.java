package com.quizplatform.quizservice.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {


    private UUID id;
    private String name;
    private String slug;
    private String iconUrl;
    private String colorHex;
    private Integer orderIndex;

    

    
}
