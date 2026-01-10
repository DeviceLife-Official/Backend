package com.devicelife.devicelife_api.domain.lifestyle.response;

import com.devicelife.devicelife_api.domain.lifestyle.request.LifestyleFeaturedDeviceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LifestyleFeaturedResponse {
    private String tagKey;
    private String tagLabel;
    private String tagType;
    private List<LifestyleFeaturedDeviceDto> devices; // 0~3 (데이터 없으면 빈 배열)
}
