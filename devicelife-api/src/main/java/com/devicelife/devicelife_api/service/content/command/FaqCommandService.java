package com.devicelife.devicelife_api.service.content.command;

import com.devicelife.devicelife_api.common.exception.CustomException;
import com.devicelife.devicelife_api.common.exception.ErrorCode;
import com.devicelife.devicelife_api.domain.content.Faq;
import com.devicelife.devicelife_api.domain.content.dto.req.FaqSaveRequestDto;
import com.devicelife.devicelife_api.domain.content.dto.res.FaqResponseDto;
import com.devicelife.devicelife_api.repository.content.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FaqCommandService {

    private final FaqRepository faqRepository;

    public FaqResponseDto createFaq(FaqSaveRequestDto request) {
        Integer sortOrder = request.getSortOrder() != null ? request.getSortOrder() : 0;
        boolean isPublished = request.getIsPublished() != null ? request.getIsPublished() : false;

        Faq faq = Faq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .sortOrder(sortOrder)
                .isPublished(isPublished)
                .build();

        Faq savedFaq = faqRepository.save(faq);

        return FaqResponseDto.from(savedFaq);
    }

    public FaqResponseDto updateFaq(Long faqId, FaqSaveRequestDto request) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAQ_4041));

        Integer sortOrder = request.getSortOrder() != null ? request.getSortOrder() : faq.getSortOrder();
        boolean isPublished = request.getIsPublished() != null ? request.getIsPublished() : faq.isPublished();
        faq.update(request.getQuestion(), request.getAnswer(), sortOrder, isPublished);

        return FaqResponseDto.from(faq);
    }

    public void deleteFaq(Long faqId) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAQ_4041));

        faqRepository.delete(faq);
    }
}
