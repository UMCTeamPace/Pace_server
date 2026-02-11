package com.example.pace.domain.schedule.dto.request;

import com.example.pace.domain.schedule.enums.SearchWay;
import com.example.pace.domain.schedule.enums.TransitType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;


public class RouteSaveReqDto {

    //유저->API (프론트 위/경도 반환)
    public record CreateRouteDTO(
            //프론트에서 받아올 정보
            BigDecimal originLat,
            BigDecimal originLng,
            BigDecimal destLat,
            BigDecimal destLng,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime arrivalTime,

            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime departureTime,

            //교통정보 지정 (버스/지하철/도보)
            TransitType transitType,
            //검색정보 지정(최적경로순/최소환승순/최소도보순)
            SearchWay searchWay

    ) {
    }

}
