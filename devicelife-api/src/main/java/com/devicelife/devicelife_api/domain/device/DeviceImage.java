package com.devicelife.devicelife_api.domain.device;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deviceImages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceImageId")
    private Long deviceImageId;

    @Column(name = "deviceId", nullable = false, insertable = false, updatable = false)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    @Column(name = "imageUrl", length = 500, nullable = false)
    private String imageUrl;

    @Column(name = "sortOrder", nullable = false)
    private Integer sortOrder;
}

