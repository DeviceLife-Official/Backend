package com.devicelife.devicelife_api.repository.combo;

import com.devicelife.devicelife_api.domain.combo.Combo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    Optional<Combo> findByComboId(Long comboId);

    @Query("""
        select distinct c
        from Combo c
        join fetch c.comboDevices cd
        join fetch cd.device d
        where c.comboId = :comboId
          and c.deletedAt is null
    """)
    Optional<Combo> findWithComboDevices(@Param("comboId") Long comboId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select c from Combo c
        left join fetch c.currentEvaluation
        where c.comboId = :comboId
    """)
    Optional<Combo> findByIdForUpdate(@Param("comboId") Long comboId);

    /**
     * 사용자의 활성 조합 중 동일한 이름이 존재하는지 확인 (삭제되지 않은 것만)
     */
    @Query("SELECT COUNT(c) > 0 FROM Combo c WHERE c.userId = :userId AND c.comboName = :comboName AND c.deletedAt IS NULL")
    boolean existsByUserIdAndComboNameAndDeletedAtIsNull(@Param("userId") Long userId, @Param("comboName") String comboName);

    /**
     * 사용자의 활성 조합 중 특정 조합을 제외하고 동일한 이름이 존재하는지 확인 (삭제되지 않은 것만)
     */
    @Query("SELECT COUNT(c) > 0 FROM Combo c WHERE c.userId = :userId AND c.comboName = :comboName AND c.comboId <> :comboId AND c.deletedAt IS NULL")
    boolean existsByUserIdAndComboNameExcludingComboId(@Param("userId") Long userId, @Param("comboName") String comboName, @Param("comboId") Long comboId);
}
