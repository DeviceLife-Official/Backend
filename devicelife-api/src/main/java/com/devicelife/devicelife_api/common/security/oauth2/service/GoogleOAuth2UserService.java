package com.devicelife.devicelife_api.common.security.oauth2.service;

import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.user.AuthProvider;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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

        return userRepository.findByEmailAndProvider(email,provider)
                .map(CustomUserDetails::new)
                // 신규 소셜 유저인 경우
                .orElseGet(() -> {
                    // 이미 일반회원가입된 유저일 경우 계정 연동해주기
                    if (userRepository.existsByEmailAndProvider(email,AuthProvider.GENERAL)) {
                        userRepository.findByEmailAndProvider(email, AuthProvider.GENERAL);
                    }

                    /*
                    if (userRepository.existsByNickname(name)) {
                        throw new OAuth2AuthenticationException("이미 사용 중인 닉네임입니다.");
                    }*/
                    User newUser = userRepository.save(User.builder()


                            .build());
                    return new CustomUserDetails(newUser);
                }

    });
}
