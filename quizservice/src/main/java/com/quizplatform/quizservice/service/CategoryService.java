package com.quizplatform.quizservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quizplatform.quizservice.dto.CategoryDto;
import com.quizplatform.quizservice.entity.Category;
import com.quizplatform.quizservice.repository.CategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {



    private final CategoryRepository categoryRepository;


    public List<CategoryDto> getAllCategories() {
        

        return categoryRepository.findByIsActiveTrueOrderByOrderIndexAsc()
        .stream()
        .map(c->CategoryDto.builder()
        .id(c.getId())
        .name(c.getName())
        .slug(c.getSlug())
        .iconUrl(c.getIconUrl())
        .colorHex(c.getColorHex())
        .orderIndex(c.getOrderIndex())
        .build())
        .toList();


    }


    
}
