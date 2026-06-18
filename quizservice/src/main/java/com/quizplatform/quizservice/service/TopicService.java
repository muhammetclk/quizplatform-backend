package com.quizplatform.quizservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.quizplatform.quizservice.dto.TopicDto;
import com.quizplatform.quizservice.repository.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;



    public List<TopicDto> getAllTopicsByCategoryId(UUID categoryId) {

        return topicRepository.findByCategoryIdAndIsActiveTrueOrderByOrderIndexAsc(categoryId).stream()
        .map(t->TopicDto.builder()
        .id(t.getId())
        .name(t.getName())
        .slug(t.getSlug())
        .description(t.getDescription())
        .difficultyLevel(t.getDifficultyLevel())
        .orderIndex(t.getOrderIndex())
        .build())
        .toList();
    }


    
}
