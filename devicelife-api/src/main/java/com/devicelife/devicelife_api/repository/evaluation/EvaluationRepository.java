package com.devicelife.devicelife_api.repository.evaluation;

import com.devicelife.devicelife_api.domain.evaluation.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByComboIdAndVersion(Long comboId, Long version);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Evaluation e where e.comboId = :comboId")
    int deleteAllByComboId(@Param("comboId") Long comboId);

    // 콤보 ID로 최신 평가 내역 조회
    Optional<Evaluation> findByComboId(Long comboId);

}
