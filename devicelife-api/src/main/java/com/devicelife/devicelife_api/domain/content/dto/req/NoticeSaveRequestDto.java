package com.devicelife.devicelife_api.domain.content.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeSaveRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "본문은 필수입니다.")
    private String body;

    @NotNull(message = "게시 여부는 필수입니다.")
    private Boolean isPublished;
}
