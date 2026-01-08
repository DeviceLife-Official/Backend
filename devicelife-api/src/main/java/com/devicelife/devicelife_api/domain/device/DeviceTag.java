package com.devicelife.devicelife_api.domain.device;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deviceTags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceTag {

    @EmbeddedId
    private DeviceTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deviceId")
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;
}

