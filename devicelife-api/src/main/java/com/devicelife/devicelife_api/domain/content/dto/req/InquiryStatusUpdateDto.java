package com.devicelife.devicelife_api.domain.content.dto.req;

import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryStatusUpdateDto {

    @NotNull(message = "상태는 필수입니다.")
    private InquiryStatus status;
}
