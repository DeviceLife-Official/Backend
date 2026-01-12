package com.devicelife.devicelife_api.domain.content.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FaqSaveRequestDto {

    @NotBlank(message = "질문은 필수입니다.")
    @Size(max = 255, message = "질문은 255자 이하여야 합니다.")
    private String question;

    @NotBlank(message = "답변은 필수입니다.")
    private String answer;

    private Integer sortOrder;

    private Boolean isPublished;
}
