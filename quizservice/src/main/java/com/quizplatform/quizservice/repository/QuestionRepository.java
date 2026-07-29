package com.quizplatform.quizservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizplatform.quizservice.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByQuizIdOrderByOrderIndexAsc(UUID quizId);
}