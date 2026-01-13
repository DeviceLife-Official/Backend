package com.devicelife.devicelife_api.repository.user;

import com.devicelife.devicelife_api.domain.user.RefreshToken;
import com.devicelife.devicelife_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByTokenHash(String tokenHash);

    User findByTokenHash(String tokenHash);
    void deleteByTokenHash(String tokenHash);
}
