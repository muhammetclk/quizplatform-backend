package com.quizplatform.quizservice.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.quizplatform.quizservice.dto.OptionDto;
import com.quizplatform.quizservice.dto.QuestionDto;
import com.quizplatform.quizservice.dto.QuizDetailDto;
import com.quizplatform.quizservice.dto.QuizSummaryDto;
import com.quizplatform.quizservice.entity.Quiz;
import com.quizplatform.quizservice.repository.QuizRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class QuizService {

private final QuizRepository quizRepository;


    public List<QuizSummaryDto> getQuizzesByTopicId(UUID topicId) {

       return quizRepository.findByTopicIdAndIsPublishedTrueOrderByCreatedAtDesc(topicId)
        .stream()
                .map(this::toSummaryDto)
                .toList();

        
    }


    public QuizDetailDto getQuizDetail(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz bulunamadı"));


                return QuizDetailDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .difficulty(quiz.getDifficulty().name())
                .timeLimitSec(quiz.getTimeLimitSec())
                .questions(quiz.getQuestions().stream()
                        .map(q -> QuestionDto.builder()
                                .id(q.getId())
                                .content(q.getContent())
                                .imageUrl(q.getImageUrl())
                                .difficulty(q.getDifficulty().name())
                                .orderIndex(q.getOrderIndex())
                                .options(q.getOptions().stream()
                                .map(o -> OptionDto.builder()
                                                .id(o.getId())
                                                .content(o.getContent())
                                                .orderIndex(o.getOrderIndex())
                                                .build())
                                        .toList())
                                .build())
                        .toList())
                .build();
    }


     private QuizSummaryDto toSummaryDto(Quiz quiz) {
        return QuizSummaryDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .difficulty(quiz.getDifficulty().name())
                .timeLimitSec(quiz.getTimeLimitSec())
                .questionCount(quiz.getQuestions() != null ? quiz.getQuestions().size() : 0)
                .createdAt(quiz.getCreatedAt())
                .build();
    }
    
    
}
