package com.quizplatform.quizservice.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
     @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.IN_PROGRESS;

    @Builder.Default
    private BigDecimal score = BigDecimal.ZERO;

    @Builder.Default
    private Integer correctCount = 0;

    @Builder.Default
    private Integer wrongCount = 0;

    @Builder.Default
    private Integer emptyCount = 0;

    private Integer timeSpentSec;

    @Column(updatable = false)
    @Builder.Default
    private LocalDateTime startedAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL)
    private List<AttemptAnswer> answers;

    public enum Status {
        IN_PROGRESS, COMPLETED, TIMEOUT, ABANDONED
    }
}
