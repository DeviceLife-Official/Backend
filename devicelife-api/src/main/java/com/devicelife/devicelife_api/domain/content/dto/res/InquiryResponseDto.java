package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Inquiry;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryResponseDto {

    private Long inquiryId;
    private String subject;
    private InquiryStatus status;
    private LocalDateTime createdAt;

    public static InquiryResponseDto from(Inquiry inquiry) {
        return InquiryResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .subject(inquiry.getSubject())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
