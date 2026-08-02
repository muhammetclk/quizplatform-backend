package com.quizplatform.quizservice.kafka;

import com.quizplatform.quizservice.dto.QuizAttemptEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuizCompletedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendQuizCompleted(QuizAttemptEventDto event) {
        try {
            kafkaTemplate.send("quiz.completed", event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("Kafka event gönderilemedi (AI açıklaması atlandı): {}", ex.getMessage());
                    } else {
                        log.info("Quiz completed event gönderildi: userId={}, quizId={}, attemptId={}",
                            event.getUserId(), event.getQuizId(), event.getAttemptId());
                    }
                });
        } catch (Exception e) {
            log.warn("Kafka bağlantısı yok, AI açıklaması atlandı: {}", e.getMessage());
        }
    }
}