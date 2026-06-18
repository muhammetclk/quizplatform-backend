package com.quizplatform.quizservice.controller;

import java.net.ResponseCache;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;

import com.quizplatform.quizservice.dto.TopicDto;
import com.quizplatform.quizservice.exception.ApiResponse;
import com.quizplatform.quizservice.service.TopicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;


    @GetMapping("/{categoryId}/topic")
    public ResponseEntity<ApiResponse<List<TopicDto>>> getAllTopics(@PathVariable UUID categoryId) {

       List<TopicDto> topics =  topicService.getAllTopicsByCategoryId(categoryId);

       return ResponseEntity.ok(ApiResponse.success(topics,"Topics are listed."));

    }



    
}
