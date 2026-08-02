package com.quizplatform.aiservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_explanations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiExplanation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID questionId;

    @Column(nullable = false, length = 1000)
    private String selectedOptionContent;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String explanationText;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
