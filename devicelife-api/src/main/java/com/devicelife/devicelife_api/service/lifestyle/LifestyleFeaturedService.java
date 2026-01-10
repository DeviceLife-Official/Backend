package com.devicelife.devicelife_api.service.lifestyle;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.lifestyle.response.LifestyleFeaturedResponse;
import com.devicelife.devicelife_api.repository.lifestyle.LifestyleFeaturedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LifestyleFeaturedService {

    private final LifestyleFeaturedRepository repo;

    public LifestyleFeaturedResponse getFeaturedByTagKey(String tagKey) {
        var tag = repo.findTagByKey(tagKey);
        if (tag == null) throw new CustomException(ErrorCode.REQ_4002, "존재하지 않는 tagKey");

        var devices = repo.findFeaturedDevicesByTagKey(tagKey);
        return new LifestyleFeaturedResponse(tag.tagKey(), tag.tagLabel(), tag.tagType(), devices);
    }

    @Transactional
    public void setFeatured(String tagKey, Long d1, Long d2, Long d3) {
        // 기기 중복 금지
        Set<Long> uniq = new HashSet<>(List.of(d1, d2, d3));
        if (uniq.size() != 3) throw new CustomException(ErrorCode.REQ_4002, "deviceId 중복");

        var tag = repo.findTagByKey(tagKey);
        if (tag == null) throw new CustomException(ErrorCode.REQ_4002, "존재하지 않는 tagKey");

        List<Long> ids = List.of(d1, d2, d3);
        if (!repo.existsAllDevices(ids)) throw new CustomException(ErrorCode.REQ_4002, "존재하지 않는 deviceId 포함");

        Long featuredSetId = repo.upsertFeaturedSet(tag.tagId());
        if (featuredSetId == null) throw new CustomException(ErrorCode.SERVER_5001, "featuredSet 생성 실패");

        // slot 1~3으로 저장 (박스들의 인덱스)
        repo.replaceSetDevices(featuredSetId, ids);
    }
}