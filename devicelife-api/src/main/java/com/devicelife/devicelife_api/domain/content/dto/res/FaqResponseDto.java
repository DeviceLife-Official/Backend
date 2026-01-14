package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Faq;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FaqResponseDto {

    private Long faqId;
    private String question;
    private String answer;
    private int sortOrder;
    private boolean isPublished;

    public static FaqResponseDto from(Faq faq) {
        return FaqResponseDto.builder()
                .faqId(faq.getFaqId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .sortOrder(faq.getSortOrder())
                .isPublished(faq.isPublished())
                .build();
    }
}
