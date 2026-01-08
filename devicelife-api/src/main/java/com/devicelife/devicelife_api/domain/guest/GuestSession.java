package com.devicelife.devicelife_api.domain.guest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "guestSessions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestSession {

    @Id
    @Column(name = "guestSessionId", columnDefinition = "CHAR(36)")
    private UUID guestSessionId;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "lastSeenAt", nullable = false)
    private LocalDateTime lastSeenAt;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (guestSessionId == null) {
            guestSessionId = UUID.randomUUID();
        }
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (lastSeenAt == null) {
            lastSeenAt = now;
        }
    }
}

