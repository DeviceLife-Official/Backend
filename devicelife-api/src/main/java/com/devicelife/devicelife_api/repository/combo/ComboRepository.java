package com.devicelife.devicelife_api.repository.combo;

import com.devicelife.devicelife_api.domain.combo.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ComboRepository extends JpaRepository<Combo, Long> {

    /**
     * 사용자의 활성 조합 목록 조회 (삭제되지 않은 것만)
     */
    @Query("SELECT c FROM Combo c WHERE c.userId = :userId AND c.deletedAt IS NULL ORDER BY c.pinnedAt DESC NULLS LAST, c.createdAt DESC")
    List<Combo> findActiveCombosByUserId(@Param("userId") Long userId);

    /**
     * 사용자의 휴지통 조합 목록 조회 (삭제된 것만)
     */
    @Query("SELECT c FROM Combo c WHERE c.userId = :userId AND c.deletedAt IS NOT NULL ORDER BY c.deletedAt DESC")
    List<Combo> findDeletedCombosByUserId(@Param("userId") Long userId);

    /**
     * 활성 조합 단건 조회 (삭제되지 않은 것만)
     */
    @Query("SELECT c FROM Combo c WHERE c.comboId = :comboId AND c.deletedAt IS NULL")
    Optional<Combo> findActiveComboById(@Param("comboId") Long comboId);

    /**
     * 30일 이상 지난 삭제된 조합 조회 (자동 삭제 대상)
     */
    @Query("SELECT c FROM Combo c WHERE c.deletedAt IS NOT NULL AND c.deletedAt < :threshold")
    List<Combo> findCombosToAutoDelete(@Param("threshold") LocalDateTime threshold);
}
