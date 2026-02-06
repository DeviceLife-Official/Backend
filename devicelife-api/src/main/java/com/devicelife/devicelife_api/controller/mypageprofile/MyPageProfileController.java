package com.devicelife.devicelife_api.controller.mypageprofile;

import com.devicelife.devicelife_api.common.response.ApiResponse;
import com.devicelife.devicelife_api.common.security.CustomUserDetails;
import com.devicelife.devicelife_api.domain.user.dto.MyPageProfileDto;
import com.devicelife.devicelife_api.service.mypageprofile.MypageProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "JWT TOKEN")
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

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4041",
                    description = "해당 email을 사용하는 사용자가 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4004",
                    description = "닉네임 미입력",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4002",
                    description = "이메일 미입력",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4003",
                    description = "소셜 유저의 경우 email 수정 불가",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4009",
                    description = "이미 사용중인 email",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "TAG_4002",
                    description = "라이프스타일은 최대 1개만 선택가능",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PatchMapping("/user-profile")
    @Operation(summary = "프로필 수정 API", description = """
            - 닉네임, 이메일, 라이프스타일 태그 수정
            - 단, 소셜 유저일경우, 이메일 수정 불가
            (AuthProvider가 GENERAL이면 일반유저, GOOGLE,APPLE이면 소셜유저)
            - 수정하지 않는 값은 null로 입력
            - 라이프스타일 태그는 2개 이상 삽입 불가
            
            """)
    public ApiResponse<MyPageProfileDto.myProfileModifyResDto> modifyUserProfile(
            @RequestBody @Valid MyPageProfileDto.myProfileModifyReqDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ApiResponse.success(
                COMMON_2001.getCode(),
                COMMON_2001.getMessage(),
                mypageProfileService.modifyMyProfileInfo(dto,customUserDetails));
    }

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "성공 ",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4005",
                    description = "소셜 로그인으로 신규 가입한 유저는 비밀번호 존재 X",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4006",
                    description = "기존 비밀번호 일치 X",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4007",
                    description = "새 비밀번호와 새 비밀번호 확인 미일치",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "USER_4008",
                    description = "새 비밀번호는 기존 비밀번호와 다르게 설정해야함",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
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
