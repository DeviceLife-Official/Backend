package com.devicelife.devicelife_api.repository.combo;

import com.devicelife.devicelife_api.domain.combo.Combo;
import com.devicelife.devicelife_api.domain.combo.ComboDevice;
import com.devicelife.devicelife_api.domain.combo.ComboDeviceId;
import com.devicelife.devicelife_api.domain.device.enums.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComboDeviceRepository extends JpaRepository<ComboDevice, ComboDeviceId> {

    /**
     * 특정 조합의 모든 기기 조회
     */
    @Query("SELECT cd FROM ComboDevice cd " +
           "JOIN FETCH cd.device d " +
           "JOIN FETCH d.brand " +
           "WHERE cd.id.comboId = :comboId " +
           "ORDER BY cd.addedAt DESC")
    List<ComboDevice> findAllByComboId(@Param("comboId") Long comboId);

    /**
     * 특정 조합의 기기 개수 조회
     */
    @Query("SELECT COUNT(cd) FROM ComboDevice cd WHERE cd.id.comboId = :comboId")
    Long countByComboId(@Param("comboId") Long comboId);

    /**
     * 조합에 특정 기기가 있는지 확인
     */
    boolean existsById(ComboDeviceId id);

    /**
     * 특정 조합의 모든 기기 삭제
     */
    void deleteAllByIdComboId(Long comboId);

    boolean existsByCombo_ComboIdAndDevice_DeviceType(Long comboComboId, DeviceType deviceDeviceType);

    boolean existsByComboAndDevice_DeviceType(Combo combo, DeviceType deviceDeviceType);
}
