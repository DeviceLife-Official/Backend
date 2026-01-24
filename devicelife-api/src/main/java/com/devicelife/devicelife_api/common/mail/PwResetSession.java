package com.devicelife.devicelife_api.common.mail;

import java.time.Instant;

public class PwResetSession {
    public String email;

    public String code;
    public Instant codeExpiresAt;

    public int tries;           // 코드 입력 시도횟수 제한
    public int resendCount;     // 재전송 횟수 제한
    public Instant lastSentAt;  // 재전송 쿨타임

    // Step2 통과 후 발급
    public String verifiedToken;
    public Instant verifiedExpiresAt;

    public static final String KEY = "PW_RESET_SESSION";
}
