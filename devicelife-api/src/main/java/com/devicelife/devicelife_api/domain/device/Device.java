package com.devicelife.devicelife_api.domain.device;

import com.devicelife.devicelife_api.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "devices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId")
    private Long deviceId;

    @Column(name = "categoryId", nullable = false, insertable = false, updatable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(name = "brandId", nullable = false, insertable = false, updatable = false)
    private Long brandId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @Column(name = "modelName", length = 200, nullable = false)
    private String modelName;

    @Column(name = "modelCode", length = 120)
    private String modelCode;

    @Column(name = "releaseDate")
    private LocalDate releaseDate;

    @Column(name = "msrp")
    private Integer msrp;

    @Column(name = "weightGram")
    private Integer weightGram;

    @Column(name = "batteryMah")
    private Integer batteryMah;

    @Column(name = "ramGb")
    private Integer ramGb;

    @Column(name = "storageGb")
    private Integer storageGb;

    @Column(name = "screenInch", precision = 4, scale = 2)
    private BigDecimal screenInch;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specJson", columnDefinition = "json")
    private Map<String, Object> specJson;
}

