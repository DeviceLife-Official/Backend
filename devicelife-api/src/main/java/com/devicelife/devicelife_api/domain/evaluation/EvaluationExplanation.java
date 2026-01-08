package com.devicelife.devicelife_api.domain.evaluation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "evaluationExplanations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationExplanation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "explanationId")
    private Long explanationId;

    @Column(name = "evaluationId", nullable = false, unique = true, insertable = false, updatable = false)
    private Long evaluationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluationId", nullable = false)
    private Evaluation evaluation;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pros", columnDefinition = "json")
    private Map<String, Object> pros;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cons", columnDefinition = "json")
    private Map<String, Object> cons;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "bestFor", columnDefinition = "json")
    private Map<String, Object> bestFor;

    @Column(name = "promptVersion", length = 20, nullable = false)
    @Builder.Default
    private String promptVersion = "p1";

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

