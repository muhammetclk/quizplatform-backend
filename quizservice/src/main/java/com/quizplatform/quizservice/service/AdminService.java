package com.quizplatform.quizservice.service;

import com.quizplatform.quizservice.dto.*;
import com.quizplatform.quizservice.entity.*;
import com.quizplatform.quizservice.repository.CategoryRepository;
import com.quizplatform.quizservice.repository.QuizRepository;
import com.quizplatform.quizservice.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Admin-only service for creating and managing quiz content.
 * All mutation operations are wrapped in a single transaction.
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final CategoryRepository categoryRepository;
    private final TopicRepository    topicRepository;
    private final QuizRepository     quizRepository;

    // ── Category ─────────────────────────────────────────────────────────────

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest req) {
        String slug = resolveSlug(req.getSlug(), req.getName());

        Category category = Category.builder()
                .name(req.getName())
                .slug(slug)
                .iconUrl(req.getIconUrl())
                .colorHex(req.getColorHex())
                .orderIndex(req.getOrderIndex() != null ? req.getOrderIndex() : 0)
                .build();

        category = categoryRepository.save(category);

        return toCategoryDto(category);
    }

    // ── Topic ─────────────────────────────────────────────────────────────────

    @Transactional
    public TopicDto createTopic(CreateTopicRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Kategori bulunamadı: " + req.getCategoryId()));

        String slug = resolveSlug(req.getSlug(), req.getName());

        Topic topic = Topic.builder()
                .category(category)
                .name(req.getName())
                .slug(slug)
                .description(req.getDescription())
                .difficultyLevel(req.getDifficultyLevel() != null
                        ? req.getDifficultyLevel()
                        : Topic.DifficultyLevel.BEGINNER)
                .orderIndex(req.getOrderIndex() != null ? req.getOrderIndex() : 0)
                .build();

        topic = topicRepository.save(topic);

        return toTopicDto(topic);
    }

    // ── Quiz ──────────────────────────────────────────────────────────────────

    @Transactional
    public QuizSummaryDto createQuiz(CreateQuizRequest req, UUID createdByUserId) {
        Topic topic = topicRepository.findById(req.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Konu bulunamadı: " + req.getTopicId()));

        Quiz.Difficulty difficulty;
        try {
            difficulty = Quiz.Difficulty.valueOf(req.getDifficulty().toUpperCase());
        } catch (IllegalArgumentException e) {
            difficulty = Quiz.Difficulty.MEDIUM;
        }

        Quiz quiz = Quiz.builder()
                .topic(topic)
                .title(req.getTitle())
                .description(req.getDescription())
                .difficulty(difficulty)
                .timeLimitSec(req.getTimeLimitSec() != null ? req.getTimeLimitSec() : 600)
                .isPublished(req.isPublished())
                .createdBy(createdByUserId)
                .build();

        // Build questions and options
        List<Question> questions = req.getQuestions().stream()
                .map(qReq -> buildQuestion(qReq, quiz))
                .toList();

        quiz.setQuestions(questions);

        Quiz saved = quizRepository.save(quiz);

        return QuizSummaryDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .difficulty(saved.getDifficulty().name())
                .timeLimitSec(saved.getTimeLimitSec())
                .questionCount(saved.getQuestions().size())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Question buildQuestion(CreateQuestionRequest qReq, Quiz quiz) {
        Question.Difficulty difficulty;
        try {
            difficulty = Question.Difficulty.valueOf(
                    (qReq.getDifficulty() != null ? qReq.getDifficulty() : "MEDIUM").toUpperCase());
        } catch (IllegalArgumentException e) {
            difficulty = Question.Difficulty.MEDIUM;
        }

        Question question = Question.builder()
                .quiz(quiz)
                .content(qReq.getContent())
                .imageUrl(qReq.getImageUrl())
                .difficulty(difficulty)
                .explanation(qReq.getExplanation())
                .orderIndex(qReq.getOrderIndex() != null ? qReq.getOrderIndex() : 0)
                .build();

        List<Option> options = qReq.getOptions().stream()
                .map(oReq -> Option.builder()
                        .question(question)
                        .content(oReq.getContent())
                        .isCorrect(Boolean.TRUE.equals(oReq.getIsCorrect()))
                        .orderIndex(oReq.getOrderIndex() != null ? oReq.getOrderIndex() : 0)
                        .build())
                .toList();

        question.setOptions(options);
        return question;
    }

    private String resolveSlug(String provided, String name) {
        if (provided != null && !provided.isBlank()) {
            return provided.toLowerCase().trim().replaceAll("\\s+", "-");
        }
        // Auto-generate from name: lowercase, replace spaces/special chars with dash
        return name.toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private CategoryDto toCategoryDto(Category c) {
        return CategoryDto.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .iconUrl(c.getIconUrl())
                .colorHex(c.getColorHex())
                .orderIndex(c.getOrderIndex())
                .build();
    }

    private TopicDto toTopicDto(Topic t) {
        return TopicDto.builder()
                .id(t.getId())
                .name(t.getName())
                .slug(t.getSlug())
                .description(t.getDescription())
                .difficultyLevel(t.getDifficultyLevel())
                .orderIndex(t.getOrderIndex())
                .build();
    }
}
