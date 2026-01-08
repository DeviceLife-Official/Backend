package com.devicelife.devicelife_api.domain.device;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deviceOffers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offerId")
    private Long offerId;

    @Column(name = "deviceId", nullable = false, insertable = false, updatable = false)
    private Long deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId", nullable = false)
    private Device device;

    @Column(name = "provider", length = 30, nullable = false)
    private String provider;

    @Column(name = "externalItemId", length = 120, nullable = false)
    private String externalItemId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "price")
    private Integer price;

    @Column(name = "currency", length = 10, nullable = false)
    @Builder.Default
    private String currency = "KRW";

    @Column(name = "productUrl", length = 600)
    private String productUrl;

    @Column(name = "imageUrl", length = 500)
    private String imageUrl;

    @Column(name = "collectedAt", nullable = false)
    private LocalDateTime collectedAt;

    @PrePersist
    protected void onCreate() {
        if (collectedAt == null) {
            collectedAt = LocalDateTime.now();
        }
    }
}

