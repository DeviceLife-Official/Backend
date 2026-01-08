package com.devicelife.devicelife_api.domain.guest;

import com.devicelife.devicelife_api.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "guestData")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestData extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestDataId")
    private Long guestDataId;

    @Column(name = "guestSessionId", columnDefinition = "CHAR(36)", nullable = false)
    private UUID guestSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestSessionId", referencedColumnName = "guestSessionId", 
                insertable = false, updatable = false)
    private GuestSession guestSession;

    @Column(name = "dataType", length = 30, nullable = false)
    private String dataType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private Map<String, Object> payload;
}

