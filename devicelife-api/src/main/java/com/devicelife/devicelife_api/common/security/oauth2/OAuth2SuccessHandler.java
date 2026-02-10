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
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

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

        String redirectUri = request.getParameter("redirect_uri");
        String target = resolveTarget(redirectUri);

        // 로컬 여부 판단(redirect_uri 기준)
        boolean isLocal = target.startsWith("http://localhost:5173");

        ResponseCookie refreshCookie;
        if (isLocal) {
            // 로컬(http) 개발용: SameSite=Lax + Secure=false + Domain 미설정
            refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(60L * 60 * 24 * 30) // 30일
                    .build();
        } else {
            // 운영(https)용: SameSite=None + Secure=true + Domain 설정
            refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .domain(".devicelife.site")
                    .path("/")
                    .maxAge(60L * 60 * 24 * 30) // 30일
                    .build();
        }

        response.sendRedirect(target);
    }

    private String resolveTarget(String redirectUri) {
        // 기본값: 운영 프론트
        String defaultTarget = "https://devicelife.site/auth/callback/google";

        if (redirectUri == null || redirectUri.isBlank()) {
            return defaultTarget;
        }

        // 허용된 프론트만 리다이렉트
        if (redirectUri.startsWith("https://devicelife.site")
                || redirectUri.startsWith("http://localhost:5173")) {
            return redirectUri;
        }

        return defaultTarget;
    }

}
