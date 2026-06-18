package com.quizplatform.quizservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizplatform.quizservice.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findByCategoryIdAndIsActiveTrueOrderByOrderIndexAsc(UUID categoryId); 


    
}
