package com.quizplatform.quizservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizplatform.quizservice.dto.CategoryDto;
import com.quizplatform.quizservice.exception.ApiResponse;
import com.quizplatform.quizservice.service.CategoryService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {

      List<CategoryDto> categories = categoryService.getAllCategories();

      return ResponseEntity.ok(ApiResponse.success(categories,"Categories are listed."));

    }
    
}
