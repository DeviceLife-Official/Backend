package com.devicelife.devicelife_api.domain.device;

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
public class DeviceTagId implements Serializable {

    @Column(name = "deviceId")
    private Long deviceId;

    @Column(name = "tagId")
    private Long tagId;
}

