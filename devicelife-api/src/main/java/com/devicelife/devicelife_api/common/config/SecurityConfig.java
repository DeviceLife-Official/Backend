package com.devicelife.devicelife_api.common.config;

import com.devicelife.devicelife_api.common.security.InternalTokenAuthFilter;
import com.devicelife.devicelife_api.common.security.JwtAuthFilter;
import com.devicelife.devicelife_api.common.security.JwtUtil;
import com.devicelife.devicelife_api.common.security.oauth2.OAuth2SuccessHandler;
import com.devicelife.devicelife_api.common.security.oauth2.OAuth2UserProviderRouter;
import com.devicelife.devicelife_api.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2UserProviderRouter oAuth2UserProviderRouter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Value("${INTERNAL_API_TOKEN}")
    private String internalApiToken;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .addFilterBefore(internalTokenAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/combos/**",
                                "/api/tags/**",
                                "/api/auth/refresh",
                                "/api/tags/logout",
                                "/api/onboarding/**",
                                "/api/mypage/**",
                                "/api/recently-viewed/**"
                                ).authenticated()
                        .requestMatchers("/internal/**").hasRole("INTERNAL")
                        //.requestMatchers("/internal/**").permitAll()
                        //.access(new WebExpressionAuthorizationManager(
                                //"hasIpAddress('100.64.0.0/10') or hasIpAddress('127.0.0.1') or hasIpAddress('::1')"
                        //))
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("""
                {"code":"AUTH_401","message":"인증이 필요합니다.","error":null,"success":false}
              """);
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("""
                {"code":"AUTH_403","message":"권한이 없습니다.","error":null,"success":false}
              """);
                        }))

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserProviderRouter) // 사용자 정보 받아오기
                        )
                        .successHandler(oAuth2SuccessHandler) // JWT 발급 및 응답
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public InternalTokenAuthFilter internalTokenAuthFilter() {
        return new InternalTokenAuthFilter(internalApiToken);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of(
                "http://localhost:5173",
                "https://devicelife.site",
                "https://www.devicelife.site",
                "https://api.devicelife.site"
        ));
        config.setAllowedMethods(java.util.List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(java.util.List.of("Authorization", "Location", "Set-Cookie"));

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
