package com.devicelife.devicelife_api.common.constants;

import java.util.Map;

public final class KeywordMapping {

    private KeywordMapping() {
    }

    public static final Map<String, String> KOREAN_TO_ENGLISH = Map.ofEntries(
            // 삼성
            Map.entry("갤럭시", "Galaxy"),
            Map.entry("삼성", "Samsung"),
            Map.entry("플립", "Flip"),
            Map.entry("폴드", "Fold"),
            Map.entry("울트라", "Ultra"),
            Map.entry("북", "Book"),
            Map.entry("탭", "Tab"),
            Map.entry("버즈", "Buds"),

            // 애플
            Map.entry("아이폰", "iPhone"),
            Map.entry("아이패드", "iPad"),
            Map.entry("맥북", "MacBook"),
            Map.entry("애플워치", "Apple Watch"),
            Map.entry("에어팟", "AirPods"),
            Map.entry("애플", "Apple"),
            Map.entry("에어", "Air"),
            Map.entry("프로", "Pro"),
            Map.entry("맥스", "Max"),
            Map.entry("미니", "mini"),
            Map.entry("플러스", "Plus"),

            // 화웨이
            Map.entry("화웨이", "Huawei"),
            Map.entry("메이트", "Mate"),
            Map.entry("메이트패드", "MatePad"),

            // 모토로라
            Map.entry("모토로라", "Motorola"),
            Map.entry("모토", "Moto"),
            Map.entry("레이저", "Razr"),
            Map.entry("엣지", "Edge"),

            // 기타 브랜드
            Map.entry("에이수스", "ASUS"),
            Map.entry("아수스", "ASUS"),
            Map.entry("에이서", "Acer"),
            Map.entry("델", "DELL"),
            Map.entry("레노버", "Lenovo"),
            Map.entry("노키아", "Nokia"),
            Map.entry("소니", "Sony"),
            Map.entry("엘지", "LG"),
            Map.entry("엠에스아이", "MSI"),
            Map.entry("에이치티씨", "HTC"),
            Map.entry("에이치피", "HP"),
            Map.entry("마이크로소프트", "Microsoft"),
            Map.entry("비츠", "Beats"),

            // 스마트폰 시리즈
            Map.entry("노트", "Note"),
            Map.entry("네오", "Neo"),
            Map.entry("라이트", "Lite"),
            Map.entry("페", "FE"),

            // 노트북 시리즈
            Map.entry("비보북", "Vivobook"),
            Map.entry("젠북", "Zenbook"),
            Map.entry("로그", "ROG"),
            Map.entry("씽크패드", "ThinkPad"),
            Map.entry("아이디어패드", "IdeaPad"),
            Map.entry("요가", "Yoga"),
            Map.entry("그램", "gram"),
            Map.entry("서피스", "Surface"),
            Map.entry("스위프트", "Swift"),
            Map.entry("파빌리온", "Pavilion"),
            Map.entry("스펙터", "Spectre"),

            // 기타 시리즈/제품
            Map.entry("엑스페리아", "Xperia"),
            Map.entry("픽셀", "Pixel"),
            Map.entry("워치", "Watch")
    );
}
