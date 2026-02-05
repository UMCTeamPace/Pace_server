package com.example.pace.domain.schedule.service;

import com.example.pace.domain.schedule.converter.RouteResDTOConverter;
import com.example.pace.domain.schedule.dto.request.DirectionRequestDTO;
import com.example.pace.domain.schedule.dto.request.RouteSaveReqDto;
import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.domain.schedule.infrastructure.GoogleDirectionApiClient;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final GoogleDirectionApiClient googleDirectionApiClient;

    public RouteApiResDto searchRoute(RouteSaveReqDto.CreateRouteDTO request) {

        // 예외처리 - arrivalTime과 departureTime이 동시에 존재할 수 없음->이런오류가있을수가있나?
        if (request.arrivalTime() != null && request.departureTime() != null) {
            throw new IllegalArgumentException(
                    "arrivalTime과 departureTime은 동시에 사용할 수 없습니다."
            );
        }

        // 1. 공통 정보 가공 (SearchWay, TransitMode)
        String routingPreference = request.searchWay() != null
                ? request.searchWay().getGoogleValue() : null;

        String transitMode = null;
        if (request.transitType() != null && request.transitType() != TransitType.WALK) {
            transitMode = request.transitType().name().toLowerCase();
        }

        // 2. 시간 분기 (항상 transit 기준)
        Long arrivalTimeEpoch = null;
        Long departureTimeEpoch = null;

        if (request.arrivalTime() != null) {
            arrivalTimeEpoch =
                    //로컬->에포크초 변환 유틸메서드 사용
                    RouteResDTOConverter.localDateTimeToEpoch(
                            request.arrivalTime()
                    );
        } else {
            departureTimeEpoch =
                    RouteResDTOConverter.localDateTimeToEpoch(
                            request.departureTime()
                    );
        }
        // 둘 다 null이면 구글이 알아서 현재로 계산합니다.
// 3. Google 요청 DTO 생성
        DirectionRequestDTO googleReq = DirectionRequestDTO.builder()
                .origin(request.originLat() + "," + request.originLng())
                .destination(request.destLat() + "," + request.destLng())
                .arrivalTime(arrivalTimeEpoch)       // 둘 중 하나만 값 있음
                .departureTime(departureTimeEpoch)   // 둘 중 하나만 값 있음
                .routingPreference(routingPreference)
                .build();

        GoogleDirectionApiResponse googleRes =
                googleDirectionApiClient.getDirections(googleReq);

        // 5. 응답 변환
        return RouteResDTOConverter.toRouteApiResDto(googleRes);
    }
}


