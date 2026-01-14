package com.devicelife.devicelife_api.service.content.query;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Inquiry;
import com.devicelife.devicelife_api.domain.content.InquiryStatus;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryDetailResponseDto;
import com.devicelife.devicelife_api.domain.content.dto.res.InquiryListDto;
import com.devicelife.devicelife_api.repository.content.InquiryRepository;
import com.devicelife.devicelife_api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryQueryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    public InquiryListDto getMyInquiries(Long userId, InquiryStatus status, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_4041);
        }

        Page<Inquiry> inquiries;
        if (status == null) {
            inquiries = inquiryRepository.findAllByUserId(userId, pageable);
        } else {
            inquiries = inquiryRepository.findAllByUserIdAndStatus(userId, status, pageable);
        }

        return InquiryListDto.of(inquiries);
    }

    public InquiryListDto getAllInquiries(InquiryStatus status, Pageable pageable) {
        Page<Inquiry> inquiries;

        if (status == null) {
            // status가 없으면 OPEN, IN_PROGRESS만 조회
            inquiries = inquiryRepository.findAllByStatusIn(
                    List.of(InquiryStatus.OPEN, InquiryStatus.IN_PROGRESS),
                    pageable
            );
        } else {
            inquiries = inquiryRepository.findAllByStatus(status, pageable);
        }

        return InquiryListDto.of(inquiries);
    }

    public InquiryDetailResponseDto getInquiryById(Long inquiryId, Long currentUserId, boolean isAdmin) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_4041));

        // 권한 체크: 본인 또는 관리자만 조회 가능
        if (!isAdmin && !inquiry.getUserId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.AUTH_4031);
        }

        return InquiryDetailResponseDto.from(inquiry);
    }
}
