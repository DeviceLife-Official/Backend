package com.devicelife.devicelife_api.domain.combo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ComboDeviceId implements Serializable {

    @Column(name = "comboId")
    private Long comboId;

    @Column(name = "deviceId")
    private Long deviceId;
}

