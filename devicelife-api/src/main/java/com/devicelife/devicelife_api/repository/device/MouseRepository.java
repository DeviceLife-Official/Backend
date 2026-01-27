package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.Mouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouseRepository extends JpaRepository<Mouse, Long> {
}
