package com.quizplatform.aiservice.kafka;

import com.quizplatform.aiservice.dto.QuizAttemptEventDto;
import com.quizplatform.aiservice.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizCompletedConsumer {

    private final AiService aiService;

    @KafkaListener(topics = "quiz.completed", groupId = "ai-service-group")
    public void consume(QuizAttemptEventDto event) {
        log.info("Received quiz completed event for user {}", event.getUserId());
        try {
            aiService.processQuizAttempt(event);
        } catch (Exception e) {
            log.error("Error processing quiz attempt event: {}", e.getMessage(), e);
        }
    }
}
