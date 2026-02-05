package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.SearchWay;
import com.example.pace.domain.schedule.enums.TransitType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;


public class RouteSaveReqDto {

    //유저->API (프론트 위/경도 반환)
    public record CreateRouteDTO(
            //프론트에서 받아올 정보
            BigDecimal originLat,
            BigDecimal originLng,
            BigDecimal destLat,
            BigDecimal destLng,

            //도착시간 -> req api url 형식 지정
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime arrivalTime,
            //출발시간 지정
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime departureTime,
            //교통정보 지정
            TransitType transitType,
            //검색정보 지정(최적경로순/최소환승순/최소도보순)
            SearchWay searchWay

    ) {
    }

}
