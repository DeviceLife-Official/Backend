package com.devicelife.devicelife_api.service.auth;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.security.JwtUtil;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.UserRole;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.USER_4001;
import static com.devicelife.devicelife_api.common.exception.ErrorCode.USER_4041;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthDto.joinResDto join(AuthDto.joinReqDto req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CustomException(USER_4001);
        }

        User newUser = User.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
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

    public AuthDto.loginResDto login(AuthDto.loginReqDto req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(USER_4041));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())){
            throw new CustomException(USER_4001);
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtUtil.createAccessToken(userDetails);

        return AuthDto.loginResDto.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }
}
