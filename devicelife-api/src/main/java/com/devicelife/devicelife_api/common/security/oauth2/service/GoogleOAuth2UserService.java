package com.devicelife.devicelife_api.common.security.oauth2.service;

import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.common.security.oauth2.domain.OAuthResult;
import com.devicelife.devicelife_api.domain.user.AuthProvider;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.devicelife.devicelife_api.common.security.oauth2.domain.RandomNickname.generateTempNickname;

@RequiredArgsConstructor
@Service
@Transactional
public class GoogleOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");
        AuthProvider provider = AuthProvider.GOOGLE;
        Map<String, Object> attrs = new HashMap<>(oAuth2User.getAttributes());

        // 이미 구글로 가입/연동된 계정이면 그대로 로그인
        return userRepository.findByEmailAndProvider(email,provider)
                .map(user -> {
                    attrs.put("oauth_result", OAuthResult.EXISTING_SOCIAL.name());
                    return new CustomUserDetails(user, attrs);
                })
                .orElseGet(() -> {
                    // 같은 이메일의 GENERAL 계정이 있으면 연동
                    User generalUser = userRepository
                            .findByEmailAndProvider(email, AuthProvider.GENERAL)
                            .orElse(null);

                    if (generalUser != null) {
                        generalUser.setProvider(AuthProvider.GOOGLE);
                        generalUser.setProviderId(providerId);
                        attrs.put("oauth_result", OAuthResult.LINKED_GENERAL.name());
                        return new CustomUserDetails(generalUser, attrs);
                    }

                    // 소셜로그인, 일반회원가입 모두 한적 없는 신규 유저인 경우
                    User newUser = userRepository.save(User.builder()
                                    .email(email)
                                    .username(name)
                                    .providerId(providerId)
                                    .provider(provider)
                                    .nickname(generateTempNickname())
                            .build());

                    attrs.put("oauth_result", OAuthResult.NEW_SIGNUP.name());
                    return new CustomUserDetails(newUser, attrs);
                });

    }
}
