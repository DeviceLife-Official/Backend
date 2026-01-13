package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Inquiry;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryDetailResponseDto {

    private Long inquiryId;
    private Long userId;
    private String email;
    private String subject;
    private String message;
    private InquiryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InquiryDetailResponseDto from(Inquiry inquiry) {
        return InquiryDetailResponseDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .userId(inquiry.getUserId())
                .email(inquiry.getEmail())
                .subject(inquiry.getSubject())
                .message(inquiry.getMessage())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .build();
    }
}
