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

    // 비즈니스 로직 메서드

    /**
     * 조합명 수정
     */
    public void updateComboName(String comboName) {
        this.comboName = comboName;
    }

    /**
     * 소프트 삭제 (휴지통으로 이동)
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 복구 (휴지통에서 복원)
     */
    public void restore() {
        this.deletedAt = null;
    }

    /**
     * 즐겨찾기 토글
     */
    public void togglePin() {
        if (this.pinnedAt == null) {
            this.pinnedAt = LocalDateTime.now();
        } else {
            this.pinnedAt = null;
        }
    }

    /**
     * 즐겨찾기 설정
     */
    public void pin() {
        this.pinnedAt = LocalDateTime.now();
    }

    /**
     * 즐겨찾기 해제
     */
    public void unpin() {
        this.pinnedAt = null;
    }

    /**
     * 총 가격 업데이트
     */
    public void updateTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 삭제 여부 확인
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /**
     * 즐겨찾기 여부 확인
     */
    public boolean isPinned() {
        return this.pinnedAt != null;
    }
}

