package com.quizplatform.quizservice.service;


import com.quizplatform.quizservice.dto.*;
import com.quizplatform.quizservice.entity.*;
import com.quizplatform.quizservice.kafka.QuizCompletedProducer;
import com.quizplatform.quizservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final QuizAttemptRepository attemptRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    //private final AIExplanationService aiExplanationService;
    private final QuizCompletedProducer quizCompletedProducer;

    @Transactional
    public AttemptResultDto submitAttempt(UUID userId, SubmitAttemptRequest request) {

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz bulunamadı"));

        List<Question> questions = questionRepository.findByQuizIdOrderByOrderIndexAsc(quiz.getId());

        // Cevapları map'e al
        Map<UUID, UUID> answerMap = new HashMap<>();
        for (SubmitAttemptRequest.AnswerItem item : request.getAnswers()) {
            answerMap.put(item.getQuestionId(), item.getSelectedOptionId());
        }

        // Attempt oluştur
        QuizAttempt attempt = QuizAttempt.builder()
                .userId(userId)
                .quiz(quiz)
                .status(QuizAttempt.Status.COMPLETED)
                .timeSpentSec(request.getTimeSpentSec())
                .completedAt(LocalDateTime.now())
                .build();

        int correct = 0, wrong = 0, empty = 0;
        List<AttemptAnswer> attemptAnswers = new ArrayList<>();
        List<AttemptResultDto.AnswerResultDto> resultAnswers = new ArrayList<>();

        for (Question question : questions) {
            UUID selectedOptionId = answerMap.get(question.getId());

            Option correctOption = question.getOptions().stream()
                    .filter(Option::isCorrect)
                    .findFirst().orElse(null);

            boolean isCorrect = false;
            Option selectedOption = null;

            if (selectedOptionId == null) {
                empty++;
            } else {
                selectedOption = question.getOptions().stream()
                        .filter(o -> o.getId().equals(selectedOptionId))
                        .findFirst().orElse(null);
                isCorrect = selectedOption != null && selectedOption.isCorrect();
                if (isCorrect) correct++; else wrong++;
            }

            // Yanlış ise AI açıklama tetikle
          /*   if (!isCorrect) {
                aiExplanationService.requestExplanation(question);
            }*/

            AttemptAnswer answer = AttemptAnswer.builder()
                    .attempt(attempt)
                    .question(question)
                    .selectedOption(selectedOption)
                    .isCorrect(isCorrect)
                    .build();
            attemptAnswers.add(answer);

            resultAnswers.add(AttemptResultDto.AnswerResultDto.builder()
                    .questionId(question.getId())
                    .questionContent(question.getContent())
                    .selectedOptionId(selectedOptionId)
                    .correctOptionId(correctOption != null ? correctOption.getId() : null)
                    .isCorrect(isCorrect)
                    .explanation(question.getExplanation())
                    .aiExplanation(question.getAiExplanation())
                    .build());
        }

        // Skor hesapla
        BigDecimal score = questions.isEmpty() ? BigDecimal.ZERO :
                BigDecimal.valueOf(correct)
                        .divide(BigDecimal.valueOf(questions.size()), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

        attempt.setScore(score);
        attempt.setCorrectCount(correct);
        attempt.setWrongCount(wrong);
        attempt.setEmptyCount(empty);
        attempt.setAnswers(attemptAnswers);

        attemptRepository.save(attempt);

        // Kafka'ya event gönder
        List<QuizAttemptEventDto.WrongAnswerDto> wrongAnswersForEvent = new ArrayList<>();
        for (AttemptAnswer ans : attemptAnswers) {
            if (Boolean.FALSE.equals(ans.getIsCorrect()) && ans.getSelectedOption() != null) {
                Option correctOpt = ans.getQuestion().getOptions().stream().filter(Option::isCorrect).findFirst().orElse(null);
                wrongAnswersForEvent.add(QuizAttemptEventDto.WrongAnswerDto.builder()
                        .questionId(ans.getQuestion().getId())
                        .questionContent(ans.getQuestion().getContent())
                        .selectedOptionContent(ans.getSelectedOption().getContent())
                        .correctOptionContent(correctOpt != null ? correctOpt.getContent() : null)
                        .build());
            }
        }

        QuizAttemptEventDto eventDto = QuizAttemptEventDto.builder()
                .userId(userId)
                .quizId(quiz.getId())
                .attemptId(attempt.getId())
                .score(score)
                .wrongAnswers(wrongAnswersForEvent)
                .build();

        quizCompletedProducer.sendQuizCompleted(eventDto);

        return AttemptResultDto.builder()
                .attemptId(attempt.getId())
                .quizTitle(quiz.getTitle())
                .score(score)
                .correctCount(correct)
                .wrongCount(wrong)
                .emptyCount(empty)
                .totalQuestions(questions.size())
                .timeSpentSec(request.getTimeSpentSec())
                .answers(resultAnswers)
                .build();
    }
}