package com.devicelife.devicelife_api.domain.lifestyle.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeviceSearchDto {
    private Long deviceId;
    private String displayName; // brand + model
}