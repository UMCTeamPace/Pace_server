package com.example.pace.domain.transit.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransitLineColorMapper {
    // 지하철
    SUBWAY_1("#003499", "#0D3692"),
    SUBWAY_2("#35b12b", "#33A23D"),
    SUBWAY_3("#f25c2b", "#FC8332"),
    SUBWAY_4("#1e97db", "#00A3D1"),
    SUBWAY_5("#893bb6", "#8B50A4"),
    SUBWAY_6("#9a4f11", "#C55C1D"),
    SUBWAY_7("#606d00", "#54640D"),
    SUBWAY_8("#e71e6e", "#F14C82"),
    SUBWAY_9("#bf9f1e", "#AA9872"),
    INCHON_LINE_1("#6691c9", "#3681B7"),
    GYENGUI_JUNGANG_LINE("#7dc4a5", "#77C4A3"),
    GYENGCHUN_LINE("#26a97f", "#0C8E72"),
    SUIN_LINE("#edb217", "#FFBB00"),

    // 버스
    BRANCH_BUS("#00FF00", "#53B332"),
    MAINLINE_BUS("#0000FF", "#005BAC"),
    METROPOLITAN_BUS("#FF0000", "#E64839"),

    DEFAULT("", "#999999"); // 기본 색상 혹시몰라서 파운데이션에 그레이로 정해둠

    private final String googleHex;
    private final String foundationColor;

    TransitLineColorMapper(String googleHex, String foundationColor) {
        this.googleHex = googleHex;
        this.foundationColor = foundationColor;
    }

    private static final Map<String, String> MAP = new HashMap<>();

    static {
        for (TransitLineColorMapper v : values()) {
            MAP.put(v.googleHex.toUpperCase(), v.foundationColor);
        }
    }

    public static String mapToFoundation(String googleHex) {
        if (googleHex == null) {
            return DEFAULT.foundationColor;
        }
        return MAP.getOrDefault(googleHex.toUpperCase(), DEFAULT.foundationColor);
    }
}
