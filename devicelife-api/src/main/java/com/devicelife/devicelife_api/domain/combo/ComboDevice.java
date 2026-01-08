package com.devicelife.devicelife_api.domain.combo;

import com.devicelife.devicelife_api.domain.device.Device;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comboDevices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboDevice {

    @EmbeddedId
    private ComboDeviceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("comboId")
    @JoinColumn(name = "comboId", nullable = false)
    private Combo combo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deviceId")
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    @Column(name = "addedAt", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }
}

