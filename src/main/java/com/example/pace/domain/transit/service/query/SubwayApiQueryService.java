package com.example.pace.domain.transit.service.query;

import com.example.pace.domain.transit.dto.request.SubwayArrivalReqDTO;

import com.example.pace.domain.transit.dto.response.SubwayArrivalResDTO;
import com.example.pace.domain.transit.dto.response.SubwayStationResDTO;
import com.example.pace.domain.transit.exception.SubwayApiException;
import com.example.pace.domain.transit.exception.code.SubwayApiErrorCode;
import com.example.pace.domain.transit.service.SubwayNetworkService;
import com.example.pace.global.config.SubwayProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayApiQueryService {
    private final WebClient webClient;
    private final SubwayProperties subwayProperties;
    private final SubwayNetworkService subwayNetworkService;

    public List<SubwayArrivalResDTO.SubwayArrivalInfoDTO> getLiveSubwayStation(
            SubwayArrivalReqDTO.SubwayArrivalDTO request
    ) {
        String startStationName = request.getStartStationName();
        String endStationName = request.getEndStationName();
        String lineName = request.getLineName();

        String url = UriComponentsBuilder.fromUriString(subwayProperties.getApiUrl())
                .pathSegment(subwayProperties.getKey(), "json", "realtimeStationArrival",
                        "0", "10", startStationName)
                .build()
                .toUriString();

        SubwayArrivalResDTO.SubwayArrivalListDTO response = webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (res) ->
                        res.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("실시간 지하철 도착 정보 API 클라이언트 에러. status: {}, body: {}", res.statusCode(),
                                    errorBody);
                            return Mono.error(new SubwayApiException(SubwayApiErrorCode.SUBWAY_API_4XX));
                        }))
                .bodyToMono(SubwayArrivalResDTO.SubwayArrivalListDTO.class)
                .block();

        if (response == null || response.getRealtimeArrivalList() == null) {
            log.warn("{} 역에 대한 실시간 도착 정보가 없습니다.", startStationName);
            return List.of(); // 빈 리스트 반환
        }

        return response.getRealtimeArrivalList().stream()
                .filter(arrival -> isSameLine(arrival.getSubwayId(), lineName))
                .filter(arrival -> isInboundDirection(arrival, startStationName, endStationName, lineName))
                .filter(arrival -> isReachDestination(arrival, startStationName, endStationName, lineName))
                .peek(arrival -> {
                    int count = subwayNetworkService.getStationCountBetween(
                            lineName,
                            arrival.getArvlMsg3(),
                            startStationName
                    );

                    arrival.setBeforeSubwayCount(count);
                })
                .toList();
    }

    // 상행인지, 하행인지 판별(상행이라고 했을 때 true면 상행, false면 하행)
    private boolean isInboundDirection(
            SubwayArrivalResDTO.SubwayArrivalInfoDTO arrival,
            String start,
            String end,
            String lineName
    ) {
        // 출발지부터 목적지까지의 경로
        List<SubwayStationResDTO> path = subwayNetworkService.getStationsBetween(lineName, start, end);

        // 출발지 역이 목적지 역인 경우 그냥 모든 열차를 보여줌
        // 추후 상의를 통하여 어케할지 정하기
        if (path.size() == 1) {
            return true;
        }

        // 경로가 없을 경우에는 그냥 열차 정보를 주지 않음 -> 이것도 얘기를 함 해보기
        if (path.isEmpty()) {
            return false;
        }

        SubwayStationResDTO startStation = path.getFirst();
        String secondStationName = path.get(1).getStationName();

        // 다음역이 포함되어 있다면 하행으로 판별
        boolean isNextDirection = startStation.getNextStations().contains(secondStationName);

        String upOrDown = arrival.getUpdnLine();

        // 2호선만 외선/내선
        if (lineName.equals("2호선")) {
            return isNextDirection ? upOrDown.equals("외선") : upOrDown.equals("내선");
        } else {
            return isNextDirection ? upOrDown.equals("하행") : upOrDown.equals("상행");
        }
    }

    // 열차가 중간에 끊기지 않고 쭉 가는지 판별
    private boolean isReachDestination(
            SubwayArrivalResDTO.SubwayArrivalInfoDTO arrival,
            String start,
            String end,
            String lineName
    ) {
        // 열차의 종착역
        String trainDestination = arrival.getBstatnNm();

        // 출발 지점에서 종착역까지 가는 경로를 뽑기
        List<SubwayStationResDTO> path = subwayNetworkService.getStationsBetween(lineName, start, trainDestination);

        // 추출한 경로 안에 가고자 하는 목적지가 포함된 경로면, 타도 되는 열차임을 판별
        return path.stream().anyMatch(s -> s.getStationName().equals(end));
    }

    // 해당 하는 호선만 필터링
    private boolean isSameLine(String apiSubwayId, String targetLineName) {
        if (apiSubwayId == null || targetLineName == null) {
            return false;
        }

        String mappedLineName = switch (apiSubwayId) {
            case "1001" -> "1호선";
            case "1002" -> "2호선";
            case "1003" -> "3호선";
            case "1004" -> "4호선";
            case "1005" -> "5호선";
            case "1006" -> "6호선";
            case "1007" -> "7호선";
            case "1008" -> "8호선";
            case "1009" -> "9호선";
            case "1061" -> "중앙선";
            case "1063" -> "경의중앙선";
            case "1065" -> "공항철도";
            case "1067" -> "경춘선";
            case "1075" -> "수인분당선";
            case "1077" -> "신분당선";
            case "1092" -> "우이신설선";
            case "1093" -> "서해선";
            case "1081" -> "경강선";
            case "1032" -> "GTX-A";
            default -> "";
        };

        return mappedLineName.equals(targetLineName);
    }
}
