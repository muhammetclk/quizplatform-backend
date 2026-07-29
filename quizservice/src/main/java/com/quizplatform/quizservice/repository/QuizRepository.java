package com.quizplatform.quizservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizplatform.quizservice.entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, UUID>{
    
        List<Quiz> findByTopicIdAndIsPublishedTrueOrderByCreatedAtDesc(UUID topicId);

}
