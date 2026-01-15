package com.devicelife.devicelife_api.controller.mypageprofile;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.user.dto.MyPageProfileDto;
import com.devicelife.devicelife_api.service.mypageprofile.MypageProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.devicelife.devicelife_api.common.response.SuccessCode.COMMON_2001;
import static com.devicelife.devicelife_api.common.response.SuccessCode.USER_2003;

@Tag(
        name = "MyPageProfile",
        description = """
        마이페이지 유저 정보/프로필 관련 API - by 남성현
        - 유저 정보 표시 (마이페이지 좌측 사이드)
        - 프로필 수정
        - 비밀번호 수정
        """
)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageProfileController {

    private final MypageProfileService mypageProfileService;

    @GetMapping("/user-profile")
    @Operation(summary = "유저 정보 표시 API", description = "닉네임, 가입일, 이메일, 라이프스타일 태그 게시")
    public ApiResponse<MyPageProfileDto.myProfileResDto> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ApiResponse.success(
                USER_2003.getCode(),
                USER_2003.getMessage(),
                mypageProfileService.myProfileInfo(customUserDetails));
    }

    @PatchMapping("/user-profile")
    @Operation(summary = "프로필 수정 API", description = """
            - 닉네임, 이메일, 라이프스타일 태그 수정
            - 단, 소셜 유저일경우, 이메일 수정 불가
            (AuthProvider가 GENERAL이면 일반유저, GOOGLE,APPLE이면 소셜유저)
            - 수정하지 않는 값은 null로 입력
            
            """)
    public ApiResponse<MyPageProfileDto.myProfileModifyResDto> modifyUserProfile(
            @RequestBody @Valid MyPageProfileDto.myProfileModifyReqDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                mypageProfileService.modifyMyProfileInfo(dto,customUserDetails));
    }

    @PutMapping("/user-profile/password")
    @Operation(summary = "비밀번호 수정 API", description = """
            기존 비밀번호, 새 비밀번호, 새 비밀번호 확인
            소셜 유저일 경우, 이메일 수정 불가 (비밀번호가 존재하지 않기 때문)
            일반 유저였다가 소셜 연동한 유저일 경우는 가능 (소셜이지만 비밀번호가 존재하기 때문)
            """)
    public ApiResponse<Void> modifyUserPassword(
            @RequestBody @Valid MyPageProfileDto.myPasswordModifyReqDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        mypageProfileService.modifyMyPassword(dto,customUserDetails);
        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                null);
    }

}
