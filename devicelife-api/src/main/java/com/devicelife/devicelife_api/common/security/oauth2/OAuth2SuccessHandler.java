package com.devicelife.devicelife_api.common.security.oauth2;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.security.JwtUtil;
import com.devicelife.devicelife_api.common.security.SHA256;
import com.devicelife.devicelife_api.common.security.oauth2.domain.OAuthResult;
import com.devicelife.devicelife_api.domain.user.RefreshToken;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.dto.AuthDto;
import com.devicelife.devicelife_api.repository.user.RefreshTokenRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SHA256 sha256;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String resultStr = (String) userDetails.getAttributes().get("oauth_result");
        OAuthResult result = (resultStr == null) ? OAuthResult.EXISTING_SOCIAL : OAuthResult.valueOf(resultStr);


        User user = userDetails.getUser();
        String accessToken = jwtUtil.createAccessToken(userDetails);
        String refreshToken = jwtUtil.createRefreshToken(userDetails);

        refreshTokenRepository.save(RefreshToken.builder()
                        .tokenHash(sha256.encrypt(refreshToken))
                        .user(user)
                .build());

        String code;
        String message;
        switch (result) {
            case EXISTING_SOCIAL -> { code = USER_2002.getCode(); message = USER_2002.getMessage(); }
            case LINKED_GENERAL  -> { code = USER_2005.getCode(); message = USER_2005.getMessage(); }
            case NEW_SIGNUP      -> { code = USER_2001.getCode(); message = USER_2001.getMessage(); }
            default              -> { code = USER_2002.getCode(); message = USER_2002.getMessage(); }
        }


        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.success(code, message,
                        new AuthDto.loginResDto(user.getUserId(),accessToken,refreshToken))
        ));
    }

}
