package com.example.pace.domain.transit.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SubwayStationDTO {
    private String stationName; // 역 이름
    private String lineName; // 호선 이름
    private String stationCode; // 역 코드
    private String externalCode; // 외부 코드

    // 다음 역, 이전 역 목록
    private List<String> nextStations;
    private List<String> prevStations;

    // 시간표
    private SubwayTimeTableDTO timeTable;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class SubwayTimeTableDTO {
        private DayTimeTableDTO weekday; // 평일 시간표
        private DayTimeTableDTO saturday; // 토요일 시간표
        private DayTimeTableDTO holiday; // 공휴일 시간표
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class DayTimeTableDTO {
        private List<TrainInfoDTO> firstTrains; // 첫차 목록
        private List<TrainInfoDTO> lastTrains; // 막차 목록
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class TrainInfoDTO {
        private String direction; // 방향(ex: 내선, 외선, 상행, 하행)
        private String destination;
        private String time; // 시간 (HHmmss 형식, ex: 053000)
    }
}
