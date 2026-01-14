package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Inquiry;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class InquiryListDto {

    private List<InquiryResponseDto> inquiries;
    private Integer listSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;

    public static InquiryListDto of(Page<Inquiry> inquiries) {
        List<InquiryResponseDto> inquiryDtos = inquiries.getContent().stream()
                .map(InquiryResponseDto::from)
                .toList();

        return InquiryListDto.builder()
                .inquiries(inquiryDtos)
                .listSize(inquiries.getNumberOfElements())
                .totalPage(inquiries.getTotalPages())
                .totalElements(inquiries.getTotalElements())
                .isFirst(inquiries.isFirst())
                .isLast(inquiries.isLast())
                .build();
    }
}
