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
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static com.devicelife.devicelife_api.common.response.SuccessCode.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SHA256 sha256;

    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();
        String accessToken = jwtUtil.createAccessToken(userDetails);
        String refreshToken = jwtUtil.createRefreshToken(userDetails);

        refreshTokenRepository.save(RefreshToken.builder()
                        .tokenHash(sha256.encrypt(refreshToken))
                        .user(user)
                .build());

        OAuth2AuthorizationRequest authRequest =
                authorizationRequestRepository.removeAuthorizationRequest(request, response);

        String redirectUri = null;
        if (authRequest != null && authRequest.getAdditionalParameters() != null) {
            Object v = authRequest.getAdditionalParameters().get("redirect_uri");
            redirectUri = (v == null) ? null : String.valueOf(v);
        }
        String target = resolveTarget(redirectUri);

        boolean isLocal = isLocalTarget(target);

        ResponseCookie refreshCookie = buildRefreshCookie(refreshToken, isLocal);

        response.addHeader(SET_COOKIE, refreshCookie.toString());
        response.sendRedirect(target);
    }

    private boolean isLocalTarget(String target) {
        try {
            URI uri = URI.create(target);
            String host = uri.getHost();
            int port = uri.getPort();

            // localhost:5173 / 127.0.0.1:5173 둘 다 허용
            boolean isLocalHost = "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host);
            return isLocalHost && port == 5173;
        } catch (Exception e) {
            return false;
        }
    }

    private ResponseCookie buildRefreshCookie(String refreshToken, boolean isLocal) {
        if (isLocal) {
            // 로컬(http) 개발용
            return ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(60L * 60 * 24 * 30) // 30일
                    .build();
        }

        // 운영(https)용
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .domain(".devicelife.site")
                .path("/")
                .maxAge(60L * 60 * 24 * 30) // 30일
                .build();
    }

    private String resolveTarget(String redirectUri) {
        String defaultTarget = "https://devicelife.site/auth/callback/google";

        if (redirectUri == null || redirectUri.isBlank()) {
            return defaultTarget;
        }

        if (redirectUri.startsWith("https://devicelife.site")
                || redirectUri.startsWith("http://localhost:5173")
                || redirectUri.startsWith("http://127.0.0.1:5173")) {
            return redirectUri;
        }

        return defaultTarget;
    }

}
