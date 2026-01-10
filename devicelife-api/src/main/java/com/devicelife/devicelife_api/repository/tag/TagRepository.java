package com.devicelife.devicelife_api.repository.tag;

import com.devicelife.devicelife_api.domain.device.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * tagType 컬럼을 기준으로 태그를 조회합니다.
     * 예: findAllByTagType("LIFESTYLE") -> 라이프스타일 태그만 반환
     */
    List<Tag> findAllByTagType(String tagType);
    Optional<Tag> findByTagKey(String tagKey);
}
