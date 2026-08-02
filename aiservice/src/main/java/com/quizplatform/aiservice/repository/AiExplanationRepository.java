package com.quizplatform.aiservice.repository;

import com.quizplatform.aiservice.entity.AiExplanation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiExplanationRepository extends JpaRepository<AiExplanation, UUID> {
}
