package com.devicelife.devicelife_api.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@RequiredArgsConstructor
public class InternalTokenAuthFilter extends OncePerRequestFilter {

    public static final String INTERNAL_HEADER = "X-Internal-Token";

    private final String internalToken; // application.yml의 INTERNAL_API_TOKEN 값

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri == null || !uri.startsWith("/internal/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 이미 인증이 심어져 있으면 패스
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(INTERNAL_HEADER);

        if (!StringUtils.hasText(token) || !safeEquals(token, internalToken)) {
            // 내부 API는 인증 필수: 토큰 없거나 불일치면 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                {"code":"AUTH_401","message":"Invalid internal token","error":null,"success":false}
            """);
            return;
        }

        // 내부 토큰 인증 성공 -> ROLE_INTERNAL로 Authentication 심기
        var auth = new UsernamePasswordAuthenticationToken(
                "internal-service", // principal (문자열로 둠)
                null,
                List.of(new SimpleGrantedAuthority("ROLE_INTERNAL"))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    // timing attack 완화용 상수시간 비교
    private boolean safeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(aBytes, bBytes);
    }
}