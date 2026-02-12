package com.example.pace.domain.schedule.service;

import static com.example.pace.domain.schedule.enums.SearchWay.MIN_TIME;

import com.example.pace.domain.schedule.converter.RouteResDTOConverter;
import com.example.pace.domain.schedule.dto.request.DirectionRequestDTO;
import com.example.pace.domain.schedule.dto.request.RouteSaveReqDto;
import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
import com.example.pace.domain.schedule.dto.response.RouteListResDTO;
import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoResDTO;
import com.example.pace.domain.schedule.dto.response.info.TransitRouteDetailInfoResDTO;
import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.domain.schedule.infrastructure.GoogleDirectionApiClient;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import com.example.pace.domain.transit.dto.SubwayStationDTO;
import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.service.BusNetworkService;
import com.example.pace.domain.transit.service.SubwayNetworkService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteCommandService {

    private final GoogleDirectionApiClient googleDirectionApiClient;
    private final SubwayNetworkService subwayNetworkService;
    private final BusNetworkService busNetworkService;

    public RouteListResDTO searchRoute(RouteSaveReqDto.CreateRouteDTO request) {

        // 예외처리 - arrivalTime과 departureTime이 동시에 존재할 수 없음->이런오류가있을수가있나?
        if (request.arrivalTime() != null && request.departureTime() != null) {
            throw new IllegalArgumentException(
                    "arrivalTime과 departureTime은 동시에 사용할 수 없습니다."
            );
        }

        // 1. 공통 정보 가공 (SearchWay, TransitMode)
        String routingPreference = request.searchWay() != null
                ? request.searchWay().getGoogleValue() : null;

        String transitMode = request.transitType() != null
                ? request.transitType().getGoogleValue() : null;

        // 2. 시간 분기 (항상 transit 기준)
        Long arrivalTimeEpoch = null;
        Long departureTimeEpoch = null;

        if (request.arrivalTime() != null) {
            arrivalTimeEpoch = request.arrivalTime().toEpochSecond();
        } else if (request.departureTime() != null) {
            departureTimeEpoch = request.departureTime().toEpochSecond();
            log.info(departureTimeEpoch.toString());
        }

        // 둘 다 null이면 구글이 알아서 현재로 계산합니다.
        // 3. Google 요청 DTO 생성
        DirectionRequestDTO googleReq = DirectionRequestDTO.builder()
                .origin(request.originLat() + "," + request.originLng())
                .destination(request.destLat() + "," + request.destLng())
                .arrivalTime(arrivalTimeEpoch)       // 둘 중 하나만 값 있음
                .departureTime(departureTimeEpoch)   // 둘 중 하나만 값 있음
                .routingPreference(routingPreference)
                .transitMode(transitMode)
                .build();

        GoogleDirectionApiResponse googleRes =
                googleDirectionApiClient.getDirections(googleReq);

        // 1) routes 리스트 변환
        RouteListResDTO result =
                RouteResDTOConverter.toRouteListResDTO(googleRes);

        if (request.searchWay() == MIN_TIME && googleRes != null && googleRes.getRoutes() != null) {
            googleRes.getRoutes().sort(Comparator.comparing(route -> {
                if (route.getLegs() != null && !route.getLegs().isEmpty() &&
                        route.getLegs().getFirst().getDuration() != null &&
                        route.getLegs().getFirst().getDuration().getValue() != null) {
                    return route.getLegs().getFirst().getDuration().getValue();
                }
                return Long.MAX_VALUE;
            }));
        }

        // 2) BUS / SUBWAY 경로에 path 추가
        enrichTransitPath(result);

        return result;

    }

    private void enrichTransitPath(RouteListResDTO result) {
        for (RouteApiResDto route : result.getRouteApiResDtoList()) {

            for (RouteDetailInfoResDTO step : route.getRouteDetailInfoResDTOList()) {

                if (step.getTransitDetail() == null) {
                    continue;
                }

                TransitRouteDetailInfoResDTO transit = step.getTransitDetail();

                String lineName = transit.getLineName();
                String start = transit.getDepartureStop();
                String end = transit.getArrivalStop();

                if (lineName == null || start == null || end == null) {
                    continue;
                }

                // BUS 처리
                if (transit.getTransitType() == TransitType.BUS) {
                    List<String> stationPath = busNetworkService.getStationsBetween(lineName, start, end)
                            .stream()
                            .map(BusInfo::getStationName)
                            .toList();

                    transit.setStationPath(stationPath);
                }

                // SUBWAY 처리
                if (transit.getTransitType() == TransitType.SUBWAY) {
                    List<String> stationPath = subwayNetworkService.getStationsBetween(lineName, start, end)
                            .stream()
                            .map(SubwayStationDTO::getStationName)
                            .toList();

                    transit.setStationPath(stationPath);
                }


            }


        }
    }

}


