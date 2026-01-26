package com.devicelife.devicelife_api.domain.evaluation;

import com.devicelife.devicelife_api.domain.combo.Combo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "evaluations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluationId")
    private Long evaluationId;

    @Column(name = "comboId", nullable = false, insertable = false, updatable = false)
    private Long comboId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comboId", nullable = false)
    private Combo combo;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "totalScore", precision = 6, scale = 2, nullable = false)
    private BigDecimal totalScore;

    @Column(name = "scoreA", precision = 6, scale = 2)
    private BigDecimal scoreA;

    @Column(name = "scoreB", precision = 6, scale = 2)
    private BigDecimal scoreB;

    @Column(name = "scoreC", precision = 6, scale = 2)
    private BigDecimal scoreC;

    //@Column(name = "scoreD", precision = 6, scale = 2)
    //private BigDecimal scoreD;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reportJson", columnDefinition = "json")
    private Map<String, Object> reportJson;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

