package com.quizplatform.quizservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizplatform.quizservice.entity.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    List<QuizAttempt> findByUserIdOrderByStartedAtDesc(UUID userId);
    List<QuizAttempt> findByUserIdAndQuizId(UUID userId, UUID quizId);
}
