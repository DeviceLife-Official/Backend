package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeviceSortType {

    LATEST("최신순", "d.releaseDate DESC, d.deviceId DESC"),
    NAME_ASC("가나다순", "d.name ASC, d.deviceId ASC"),
    PRICE_ASC("낮은 가격순", "d.price ASC, d.deviceId ASC"),
    PRICE_DESC("높은 가격순", "d.price DESC, d.deviceId DESC");

    private final String displayName;
    private final String orderByClause;
}
