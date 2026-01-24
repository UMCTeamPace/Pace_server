package com.example.pace.domain.transit.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SubwayGraphNode {
    private final String stationName; // 역 이름
    private final String lineName; // 호선 이름
    private final String stationCode; // 전철역 코드
    private final String externalCode; // 외부 코드

    @Builder.Default
    private final List<String> nextStations = new ArrayList<>();

    @Builder.Default
    private final List<String> prevStations = new ArrayList<>();

    @Builder.Default
    private final Map<String, String> timeTable = new HashMap<>();

    // 인접 역 추가 메서드
    public void addNextStation(String name) {
        if (!nextStations.contains(name)) {
            nextStations.add(name);
        }
    }

    public void addPrevStation(String name) {
        if (!prevStations.contains(name)) {
            prevStations.add(name);
        }
    }
}
