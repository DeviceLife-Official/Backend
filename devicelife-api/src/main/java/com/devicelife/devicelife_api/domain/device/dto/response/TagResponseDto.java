package com.devicelife.devicelife_api.domain.device.dto.response;

import com.devicelife.devicelife_api.domain.device.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponseDto {
    private Long tagId;
    private String tagKey;    // 예: interest_game
    private String tagLabel;  // 예: 게임
    private String tagType;   // 예: INTEREST

    // Entity -> DTO 변환 메서드
    public static TagResponseDto from(Tag tag) {
        return TagResponseDto.builder()
                .tagId(tag.getTagId())
                .tagKey(tag.getTagKey())
                .tagLabel(tag.getTagLabel())
                .tagType(tag.getTagType())
                .build();
    }
}
