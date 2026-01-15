package com.devicelife.devicelife_api.common.security.oauth2.domain;

public enum OAuthResult {
    EXISTING_SOCIAL,   // 기존 소셜 로그인
    LINKED_GENERAL,    // GENERAL 계정을 소셜 계정으로 연동
    NEW_SIGNUP         // 완전 신규 가입
}
