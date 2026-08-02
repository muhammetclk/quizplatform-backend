package com.quizplatform.quizservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizplatform.quizservice.dto.QuizDetailDto;
import com.quizplatform.quizservice.dto.QuizSummaryDto;
import com.quizplatform.quizservice.exception.ApiResponse;
import com.quizplatform.quizservice.service.QuizService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;


    @GetMapping("/topic/{topicId}/quiz")
    public ResponseEntity<ApiResponse<List<QuizSummaryDto>>> getQuizzes(@PathVariable UUID topicId) {

        List<QuizSummaryDto> quizzes = quizService.getQuizzesByTopicId(topicId);
        return ResponseEntity.ok(ApiResponse.success(quizzes,"Quizzes are listed."));


        
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<ApiResponse<QuizDetailDto>> getQuizDetail(@PathVariable UUID quizId) {

        QuizDetailDto quiz = quizService.getQuizDetail(quizId);
        return ResponseEntity.ok(ApiResponse.success(quiz,"quiz is listed."));


        
    }


   
    


    
}
