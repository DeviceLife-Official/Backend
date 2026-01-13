package com.devicelife.devicelife_api.repository.user;

import com.devicelife.devicelife_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPhoneNumber(String username, String phoneNumber);
}
