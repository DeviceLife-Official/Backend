package com.devicelife.devicelife_api.service.auth;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.security.JwtUtil;
import com.devicelife.devicelife_api.common.security.SHA256;
import com.devicelife.devicelife_api.domain.user.RefreshToken;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.UserRole;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.repository.user.RefreshTokenRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SHA256 sha256;

    public AuthDto.joinResDto join(AuthDto.joinReqDto req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(USER_4001);
        }

        User newUser = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .role(UserRole.USER)
                .onboardingCompleted(false)
                .phoneNumber(req.getPhoneNumber())
                .build();

        User user = userRepository.save(newUser);

        return AuthDto.joinResDto.builder()
                .userId(user.getUserId())
                .build();
    }

    public AuthDto.emailDupCheckResDto emailDupCheck(AuthDto.emailDupCheckReqDto req) {

        Boolean success = !userRepository.existsByEmail(req.getEmail());

        return AuthDto.emailDupCheckResDto.builder()
                .success(success)
                .build();
    }

    public AuthDto.loginResDto login(AuthDto.loginReqDto req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(USER_4041));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())){
            throw new CustomException(USER_4006);
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.createAccessToken(userDetails);
        String refreshToken = jwtUtil.createRefreshToken(userDetails);

        refreshTokenRepository.save(RefreshToken.builder()
                .tokenHash(sha256.encrypt(refreshToken))
                .user(user)
                .build());

        return AuthDto.loginResDto.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthDto.refreshResDto refresh(String refreshToken) {

        if (!jwtUtil.isValid(refreshToken)) {
            throw new CustomException(AUTH_4012);
        }
        if (!refreshTokenRepository.existsByTokenHash(sha256.encrypt(refreshToken))) {
            throw new CustomException(AUTH_4013);
        }

        String email = jwtUtil.getEmail(refreshToken);
        log.info("refresh sub={}", email);

        User user = userRepository.findByUserId(jwtUtil.getUserId(refreshToken))
                .orElseThrow(() -> new CustomException(USER_4041));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.createAccessToken(userDetails);

        return AuthDto.refreshResDto.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .build();
    }

    public void logout(String refreshToken) {

        refreshTokenRepository.deleteByTokenHash(sha256.encrypt(refreshToken));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public AuthDto.findIdResDto findId(AuthDto.findIdReqDto req) {

        User user = userRepository.findByUsernameAndPhoneNumber(req.getUsername(), req.getPhoneNumber())
                .orElseThrow(() -> new CustomException(USER_4041));

        String email = user.getEmail();

        return AuthDto.findIdResDto.builder()
                .emailInfo(email)
                .build();
    }
}
