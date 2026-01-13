package com.devicelife.devicelife_api.service.content.query;

import com.devicelife.devicelife_api.domain.content.Faq;
import com.devicelife.devicelife_api.domain.content.dto.res.FaqResponseDto;
import com.devicelife.devicelife_api.repository.content.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqQueryService {

    private final FaqRepository faqRepository;

    public List<FaqResponseDto> getFaqs(Boolean isPublished) {
        List<Faq> faqs = faqRepository.findAllByIsPublishedOrderBySortOrderAsc(isPublished);
        return faqs.stream().map(FaqResponseDto::from).toList();
    }
}
