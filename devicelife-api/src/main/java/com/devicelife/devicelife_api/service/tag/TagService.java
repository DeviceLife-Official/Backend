package com.devicelife.devicelife_api.service.tag;

import com.devicelife.devicelife_api.domain.device.Tag;
import com.devicelife.devicelife_api.domain.device.dto.request.UserTagRequestDto;
import com.devicelife.devicelife_api.domain.device.dto.response.TagResponseDto;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.UserTag;
import com.devicelife.devicelife_api.domain.user.UserTagId;
import com.devicelife.devicelife_api.repository.tag.TagRepository;
import com.devicelife.devicelife_api.repository.tag.UserTagRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<TagResponseDto> getTags(String type) {
        List<Tag> tags;

        if (!StringUtils.hasText(type)) {
            tags = tagRepository.findAll();
        } else {
            String normalized = type.trim().toUpperCase();
            tags = tagRepository.findAllByTagType(normalized);
        }

        return tags.stream().map(TagResponseDto::from).toList();
    }

    @Transactional
    public void saveUserTags(UserTagRequestDto request) {
        Long userId = request.getUserId();

        User user = em.find(User.class, userId);
        if (user == null) {
            throw new EntityNotFoundException("존재하지 않는 userId: " + userId);
        }

        List<Long> tagIds = request.getTagIds().stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()) {
            Set<Long> found = tags.stream().map(Tag::getTagId).collect(Collectors.toSet());
            List<Long> missing = tagIds.stream().filter(id -> !found.contains(id)).toList();
            throw new EntityNotFoundException("존재하지 않는 tagIds: " + missing);
        }

        // replace 기준: 이번 요청에 포함된 tagType들을 추출
        Set<String> tagTypes = tags.stream()
                .map(t -> t.getTagType().trim().toUpperCase())
                .collect(Collectors.toSet());

        // 해당 tagType 영역은 기존 선택 싹 삭제
        userTagRepository.deleteByUserIdAndTagTypes(userId, tagTypes);

        // 새로 저장
        List<UserTag> toSave = tags.stream()
                .map(t -> UserTag.builder()
                        .id(new UserTagId(userId, t.getTagId()))
                        .user(user)
                        .tag(t)
                        .build()
                )
                .toList();

        userTagRepository.saveAll(toSave);
    }

}