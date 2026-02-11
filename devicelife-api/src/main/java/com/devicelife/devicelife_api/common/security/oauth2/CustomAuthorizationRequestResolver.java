/*package com.devicelife.devicelife_api.common.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver delegate;

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authRequest = delegate.resolve(request);
        return customize(request, authRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authRequest = delegate.resolve(request, clientRegistrationId);
        return customize(request, authRequest);
    }

    private OAuth2AuthorizationRequest customize(HttpServletRequest request, OAuth2AuthorizationRequest authRequest) {
        if (authRequest == null) return null;

        String redirectUri = request.getParameter("redirect_uri");

        // redirect_uri가 없으면 그대로(= 기본 흐름)
        if (redirectUri == null || redirectUri.isBlank()) {
            return authRequest;
        }

        Map<String, Object> additional = new HashMap<>(authRequest.getAdditionalParameters());
        additional.put("redirect_uri", redirectUri);

        return OAuth2AuthorizationRequest.from(authRequest)
                .additionalParameters(additional)
                .build();
    }
}*/
