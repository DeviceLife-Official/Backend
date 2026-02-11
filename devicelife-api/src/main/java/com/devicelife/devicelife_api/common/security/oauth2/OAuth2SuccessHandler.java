package com.devicelife.devicelife_api.common.security.oauth2;
//"http://localhost:5173/auth/callback/google"

import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.security.JwtUtil;
import com.devicelife.devicelife_api.common.security.SHA256;
import com.devicelife.devicelife_api.common.security.oauth2.domain.OAuthResult;
import com.devicelife.devicelife_api.domain.user.RefreshToken;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.user.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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

        // refreshToken을 HttpOnly 쿠키로 내려주기
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                //.secure(false)
                //.sameSite("Lax")
                .path("/")
                .domain(".devicelife.site")
                .maxAge(60L * 60 * 24 * 30) // 30일
                .build();
        response.addHeader(SET_COOKIE, refreshCookie.toString());

        String redirectUri = request.getParameter("redirect_uri");
        String target = resolveTarget(redirectUri);
        response.sendRedirect(target);
    }

    private String resolveTarget(String redirectUri) {
        // 안전장치: 허용된 프론트만 리다이렉트 가능하게
        if (redirectUri == null || redirectUri.isBlank()) {
            return "https://devicelife.site/auth/callback/google";
        }

        // 허용 목록
        if (redirectUri.startsWith("https://devicelife.site")
                || redirectUri.startsWith("http://localhost:5173")) {
            return redirectUri;
        }

        return "https://devicelife.site/auth/callback/google";
    }

}

