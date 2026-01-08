package com.devicelife.devicelife_api.domain.combo;

import com.devicelife.devicelife_api.domain.common.BaseTimeEntity;
import com.devicelife.devicelife_api.domain.evaluation.Evaluation;
import com.devicelife.devicelife_api.domain.guest.GuestSession;
import com.devicelife.devicelife_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "combos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comboId")
    private Long comboId;

    @Column(name = "userId", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "guestSessionId", columnDefinition = "CHAR(36)", insertable = false, updatable = false)
    private UUID guestSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestSessionId", referencedColumnName = "guestSessionId")
    private GuestSession guestSession;

    @Column(name = "comboName", length = 80, nullable = false)
    private String comboName;

    @Column(name = "pinnedAt")
    private LocalDateTime pinnedAt;

    @Column(name = "totalPrice", nullable = false)
    @Builder.Default
    private Integer totalPrice = 0;

    @Column(name = "currentEvaluationId", insertable = false, updatable = false)
    private Long currentEvaluationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currentEvaluationId")
    private Evaluation currentEvaluation;

    @Column(name = "currentTotalScore", precision = 6, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal currentTotalScore = BigDecimal.ZERO;

    @Column(name = "evaluatedAt")
    private LocalDateTime evaluatedAt;

    @Column(name = "evaluationVersion", length = 20, nullable = false)
    @Builder.Default
    private String evaluationVersion = "v1";

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
}

