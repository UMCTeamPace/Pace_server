package com.example.pace.domain.transit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SubwayArrivalResDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubwayArrivalListDTO {
        // 실제 json 응답 key 네임과 일치시킴
        private List<SubwayArrivalInfoDTO> realtimeArrivalList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubwayArrivalInfoDTO {
        // subwayId 값
        private String subwayId;
        // 도착지 방면
        private String trainLineNm;
        // 도착 예정 시간(단위: 초)
        private String barvlDt;
        // 몇 전역인지에 대한 메세지
        private String arvlMsg2;
        // 현재 열차가 위치한 역 이름
        private String arvlMsg3;
        // 해당 열차의 종착역
        private String bstatnNm;
        // 상/하행 구분
        private String updnLine;
        // 몇 정거장 전인지
        private Integer beforeSubwayCount;
    }
}
