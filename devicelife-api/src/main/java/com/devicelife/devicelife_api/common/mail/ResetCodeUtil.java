package com.devicelife.devicelife_api.common.mail;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

public class ResetCodeUtil {
    private static final SecureRandom RND = new SecureRandom();

    public static String generate6Digits() {
        int n = RND.nextInt(1_000_000); // 0 ~ 999999
        return String.format("%06d", n);
    }

    public static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
