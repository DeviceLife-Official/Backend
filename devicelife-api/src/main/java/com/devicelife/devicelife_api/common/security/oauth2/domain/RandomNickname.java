package com.devicelife.devicelife_api.common.security.oauth2.domain;

import java.util.UUID;

public class RandomNickname {

    public static String generateTempNickname() {
        return  "user_" + UUID.randomUUID().toString().substring(0, 6);
    }
}
