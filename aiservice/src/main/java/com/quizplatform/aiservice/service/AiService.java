package com.quizplatform.aiservice.service;

import com.quizplatform.aiservice.dto.QuizAiResultDto;
import com.quizplatform.aiservice.dto.QuizAttemptEventDto;
import com.quizplatform.aiservice.entity.AiExplanation;
import com.quizplatform.aiservice.repository.AiExplanationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final GeminiApiClient geminiApiClient;
    private final StringRedisTemplate redisTemplate;
    private final AiExplanationRepository aiExplanationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void processQuizAttempt(QuizAttemptEventDto event) {
        log.info("Processing attempt for user {}, quiz {}", event.getUserId(), event.getQuizId());

        List<QuizAiResultDto.ExplainedAnswer> explainedAnswers = new ArrayList<>();

        for (QuizAttemptEventDto.WrongAnswerDto wrongAnswer : event.getWrongAnswers()) {
            String cacheKey = "ai_expl:" + wrongAnswer.getQuestionId() + ":" +
                    (wrongAnswer.getSelectedOptionContent() != null ? wrongAnswer.getSelectedOptionContent().hashCode() : "null");

            // Cache'den dene (Redis yoksa atla)
            String explanation = null;
            try {
                explanation = redisTemplate.opsForValue().get(cacheKey);
            } catch (Exception e) {
                log.warn("Redis erişilemedi, cache atlanıyor: {}", e.getMessage());
            }

            if (explanation == null) {
                log.info("Gemini API çağrılıyor, soru: {}", wrongAnswer.getQuestionId());
                explanation = geminiApiClient.generateExplanation(
                        wrongAnswer.getQuestionContent(),
                        wrongAnswer.getSelectedOptionContent(),
                        wrongAnswer.getCorrectOptionContent()
                );

                // Redis'e kaydet (Redis yoksa hata verme)
                try {
                    redisTemplate.opsForValue().set(cacheKey, explanation, Duration.ofDays(7));
                } catch (Exception e) {
                    log.warn("Redis'e yazılamadı, devam ediliyor: {}", e.getMessage());
                }

                // DB'ye kaydet
                try {
                    AiExplanation aiExplanation = AiExplanation.builder()
                            .questionId(wrongAnswer.getQuestionId())
                            .selectedOptionContent(wrongAnswer.getSelectedOptionContent())
                            .explanationText(explanation)
                            .build();
                    aiExplanationRepository.save(aiExplanation);
                } catch (Exception e) {
                    log.warn("DB'ye yazılamadı: {}", e.getMessage());
                }
            } else {
                log.info("Cache hit for question {}", wrongAnswer.getQuestionId());
            }

            explainedAnswers.add(QuizAiResultDto.ExplainedAnswer.builder()
                    .questionId(wrongAnswer.getQuestionId())
                    .questionContent(wrongAnswer.getQuestionContent())
                    .selectedOptionContent(wrongAnswer.getSelectedOptionContent())
                    .aiExplanation(explanation)
                    .build());
        }

        QuizAiResultDto result = QuizAiResultDto.builder()
                .quizId(event.getQuizId())
                .attemptId(event.getAttemptId())
                .score(event.getScore())
                .explanations(explainedAnswers)
                .build();

        String destination = "/topic/quiz-result/" + event.getUserId();
        messagingTemplate.convertAndSend(destination, result);
        log.info("Result sent to WebSocket destination: {}", destination);
    }
}
