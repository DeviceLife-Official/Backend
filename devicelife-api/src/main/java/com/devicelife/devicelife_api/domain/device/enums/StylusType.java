package com.devicelife.devicelife_api.domain.device.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 스타일러스 종류
 */
@Getter
@RequiredArgsConstructor
public enum StylusType {

    APPLE_PENCIL_1("Apple Pencil 1세대"),
    APPLE_PENCIL_2("Apple Pencil 2세대"),
    APPLE_PENCIL_PRO("Apple Pencil Pro"),
    APPLE_PENCIL_USB_C("Apple Pencil USB-C"),
    S_PEN("S Pen"),
    SURFACE_PEN("Surface Pen"),
    USI("USI 펜"),
    NONE("미지원");

    private final String displayName;

    public boolean isApplePencil() {
        return this == APPLE_PENCIL_1 || this == APPLE_PENCIL_2 
            || this == APPLE_PENCIL_PRO || this == APPLE_PENCIL_USB_C;
    }
}
