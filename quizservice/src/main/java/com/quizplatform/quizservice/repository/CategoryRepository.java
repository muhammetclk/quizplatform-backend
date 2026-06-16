package com.quizplatform.quizservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.quizplatform.quizservice.entity.Category;



public interface CategoryRepository extends JpaRepository<Category, UUID> {

List<Category> findByIsActiveTrueOrderByOrderIndexAsc();
    
}
