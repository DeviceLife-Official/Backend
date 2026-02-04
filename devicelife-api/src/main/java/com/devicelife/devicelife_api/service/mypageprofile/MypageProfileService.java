package com.devicelife.devicelife_api.service.mypageprofile;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.device.Tag;
import com.devicelife.devicelife_api.domain.user.AuthProvider;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.domain.user.UserTag;
import com.devicelife.devicelife_api.domain.user.UserTagId;
import com.devicelife.devicelife_api.domain.user.dto.MyPageProfileDto;
import com.devicelife.devicelife_api.repository.tag.TagRepository;
import com.devicelife.devicelife_api.repository.tag.UserTagRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.devicelife.devicelife_api.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageProfileService {

    private final UserTagRepository userTagRepository;
    private final TagRepository tagRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageProfileDto.myProfileResDto myProfileInfo(
            CustomUserDetails cud) {

        User user = cud.getUser();

        List<String> lifestyleList = userTagRepository.findTagLabelsByUserIdOrderByTagLabelAsc(user.getUserId());

        return MyPageProfileDto.myProfileResDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lifestyleList(lifestyleList)
                .authProvider(user.getProvider())
                .isOnboardingCompleted(user.isOnboardingCompleted())
                .build();
    }

    public MyPageProfileDto.myProfileModifyResDto modifyMyProfileInfo(
            MyPageProfileDto.myProfileModifyReqDto req, CustomUserDetails cud) {

        Long userId = cud.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_4041));

        if (req.getUsername() != null) {
            String nickname = req.getUsername().trim();
            if (nickname.isEmpty()) {throw new CustomException(USER_4004);}
            user.setUsername(nickname);
        }

        if (req.getEmail() != null) {
            String email = req.getEmail().trim();
            if (email.isEmpty()) {throw new CustomException(USER_4002);}
            if (!user.getProvider().equals(AuthProvider.GENERAL)) {
                throw new CustomException(USER_4003);}
            if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                throw new CustomException(USER_4009);}
            user.setEmail(email);
        }

        // 3) 태그 부분 수정: null이면 변경 안 함 / []이면 전체 삭제 / 값 있으면 전체 교체
        if (req.getLifestyleList() != null) {

            // 기존 userTags 전체 삭제
            userTagRepository.deleteAllByUser(user);

            // 빈 리스트면 태그 전부 제거로 끝
            if (!req.getLifestyleList().isEmpty()) {

                //중복제거
                List<String> distinctLabels = req.getLifestyleList().stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .distinct()
                        .toList();

                List<Tag> tags = tagRepository.findByTagLabelIn(distinctLabels);

                // 유효성 검증
                if (tags.size() != distinctLabels.size()) {
                    throw new CustomException(TAG_4001);
                }

                // 새로 저장
                List<UserTag> userTags = new ArrayList<>(tags.size());
                for (Tag tag : tags) {
                    UserTagId id = new UserTagId(userId, tag.getTagId());

                    userTags.add(UserTag.builder()
                            .id(id)
                            .user(user)
                            .tag(tag)
                            .build());
                }
                userTagRepository.saveAll(userTags);
            }
        }
        List<String> tagLabels = userTagRepository.findTagLabelsByUserIdOrderByTagLabelAsc(userId);
        userRepository.save(user);

        return MyPageProfileDto.myProfileModifyResDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .lifestyleList(tagLabels)
                .build();
    }

    public void modifyMyPassword(MyPageProfileDto.myPasswordModifyReqDto req, CustomUserDetails cud) {

        User user = cud.getUser();

        // 소셜가입 유저의 경우 비밀번호가 없음
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new CustomException(USER_4005);}
        // 기존 비밀번호가 일치하지 않을 경우
        else if (!passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            throw new CustomException(USER_4006);}
        // 새 비밀번호와 비밀번호 확인이 일치하지 않을 경우
        else if (!req.getNewPassword().equals(req.getNewPasswordConfirm())) {
            throw new CustomException(USER_4007);}
        // 새 비밀번호와 기존 비밀번호가 같을 경우
        else if (passwordEncoder.matches(req.getNewPassword(), user.getPasswordHash())) {
            throw new CustomException(USER_4008);}

        String newPassword = passwordEncoder.encode(req.getNewPassword());
        user.setPasswordHash(newPassword);
        userRepository.save(user);
    }


}
