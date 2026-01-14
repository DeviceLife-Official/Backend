package com.devicelife.devicelife_api.domain.content.dto.res;

import com.devicelife.devicelife_api.domain.content.Notice;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class NoticeListDto {

    private List<NoticeResponseDto> notices;
    private Integer listSize;
    private Integer totalPage;
    private Long totalElements;
    private Boolean isFirst;
    private Boolean isLast;

    public static NoticeListDto of(Page<Notice> noticePage) {
        List<NoticeResponseDto> noticeDtos = noticePage.getContent().stream()
                .map(NoticeResponseDto::from)
                .toList();

        return NoticeListDto.builder()
                .notices(noticeDtos)
                .listSize(noticePage.getNumberOfElements())
                .totalPage(noticePage.getTotalPages())
                .totalElements(noticePage.getTotalElements())
                .isFirst(noticePage.isFirst())
                .isLast(noticePage.isLast())
                .build();
    }
}
