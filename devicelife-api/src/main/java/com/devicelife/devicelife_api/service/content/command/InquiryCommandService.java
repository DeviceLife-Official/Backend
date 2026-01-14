package com.devicelife.devicelife_api.service.content.command;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Inquiry;
import com.devicelife.devicelife_api.domain.content.dto.req.InquirySaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.req.InquiryStatusUpdateDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryDetailResponseDto;
import com.devicelife.devicelife_api.domain.user.User;
import com.devicelife.devicelife_api.repository.content.InquiryRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryCommandService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    public InquiryDetailResponseDto createInquiry(Long userId, InquirySaveRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_4041));

        Inquiry inquiry = Inquiry.builder()
                .email(request.getEmail())
                .subject(request.getSubject())
                .message(request.getMessage())
                .userId(userId)
                .user(user)
                .build();

        return InquiryDetailResponseDto.from(inquiryRepository.save(inquiry));
    }

    public InquiryDetailResponseDto updateInquiry(Long inquiryId, InquiryStatusUpdateDto request) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_4041));

        inquiry.updateStatus(request.getStatus());

        return InquiryDetailResponseDto.from(inquiry);
    }
}
