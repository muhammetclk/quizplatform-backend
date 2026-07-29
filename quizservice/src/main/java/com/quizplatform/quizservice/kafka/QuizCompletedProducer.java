package com.quizplatform.quizservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizCompletedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendQuizCompleted(UUID userId, UUID quizId, BigDecimal score) {
        Map<String, Object> event = Map.of(
                "userId", userId.toString(),
                "quizId", quizId.toString(),
                "score", score
        );
        kafkaTemplate.send("quiz.completed", event);
        log.info("Quiz completed event gönderildi: userId={}, quizId={}", userId, quizId);
    }
}