package com.devicelife.devicelife_api.repository.device;

import com.devicelife.devicelife_api.domain.device.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {
}
