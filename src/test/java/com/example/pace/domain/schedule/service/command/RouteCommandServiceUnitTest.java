package com.example.pace.domain.schedule.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.pace.domain.schedule.dto.request.DirectionRequestDTO;
import com.example.pace.domain.schedule.dto.request.RouteSaveReqDto;
import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
import com.example.pace.domain.schedule.dto.response.RouteListResDTO;
import com.example.pace.domain.schedule.dto.response.info.TransitRouteDetailInfoResDTO;
import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.domain.schedule.infrastructure.GoogleDirectionApiClient;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;
import com.example.pace.domain.transit.dto.response.SubwayStationResDTO;
import com.example.pace.domain.transit.service.SubwayNetworkService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RouteCommandServiceUnitTest {

    @InjectMocks
    private RouteCommandService routeCommandService;

    @Mock
    private GoogleDirectionApiClient googleDirectionApiClient;

    @Mock
    private SubwayNetworkService subwayNetworkService;

    @Test
    @DisplayName("지하철 하행 경로 탐색 시 downNext 필드에 하행 방향이 세팅된다.")
    void searchRoute_Subway_Downbound_Success() {
        // given: 하행 경로 조건 세팅
        RouteSaveReqDto.CreateRouteDTO request = new RouteSaveReqDto.CreateRouteDTO(
                BigDecimal.valueOf(37.1), BigDecimal.valueOf(127.1),
                BigDecimal.valueOf(37.2), BigDecimal.valueOf(127.2),
                null, null, TransitType.SUBWAY, null
        );

        GoogleDirectionApiResponse googleRes = createMockGoogleResponse("1호선", "시청", "서울역");
        given(googleDirectionApiClient.getDirections(any(DirectionRequestDTO.class)))
                .willReturn(googleRes);

        // 하행 조건: 출발역의 nextStations 리스트에 도착역 포함
        SubwayStationResDTO startStation = new SubwayStationResDTO();
        ReflectionTestUtils.setField(startStation, "stationName", "시청");
        ReflectionTestUtils.setField(startStation, "nextStations", List.of("서울역"));
        ReflectionTestUtils.setField(startStation, "prevStations", List.of("종각"));

        SubwayStationResDTO secondStation = new SubwayStationResDTO();
        ReflectionTestUtils.setField(secondStation, "stationName", "서울역");

        given(subwayNetworkService.getStationsBetween(anyString(), anyString(), anyString()))
                .willReturn(List.of(startStation, secondStation));

        // when: 서비스 메서드 실행
        RouteListResDTO result = routeCommandService.searchRoute(request);

        // then: 결과 검증
        assertThat(result.getRouteApiResDtoList()).hasSize(1);
        RouteApiResDto route = result.getRouteApiResDtoList().getFirst();

        TransitRouteDetailInfoResDTO transitDetail = route.getRouteDetailInfoResDTOList().getFirst().getTransitDetail();

        assertThat(transitDetail.getDownNext()).isEqualTo("하행 서울역");
        assertThat(transitDetail.getUpNext()).isNull();
        assertThat(transitDetail.getStationPath()).containsExactly("시청", "서울역");
    }

    @Test
    @DisplayName("지하철 상행 경로 탐색 시 upNext 필드에 상행 방향이 세팅된다.")
    void searchRoute_Subway_Upbound_Success() {
        // given: 상행 경로 조건 세팅
        RouteSaveReqDto.CreateRouteDTO request = new RouteSaveReqDto.CreateRouteDTO(
                BigDecimal.valueOf(37.1), BigDecimal.valueOf(127.1),
                BigDecimal.valueOf(37.2), BigDecimal.valueOf(127.2),
                null, null, TransitType.SUBWAY, null
        );

        GoogleDirectionApiResponse googleRes = createMockGoogleResponse("1호선", "서울역", "시청");
        given(googleDirectionApiClient.getDirections(any(DirectionRequestDTO.class)))
                .willReturn(googleRes);

        // 상행 조건: 출발역의 prevStations 리스트에 도착역 포함
        SubwayStationResDTO startStation = new SubwayStationResDTO();
        ReflectionTestUtils.setField(startStation, "stationName", "서울역");
        ReflectionTestUtils.setField(startStation, "nextStations", List.of("남영"));
        ReflectionTestUtils.setField(startStation, "prevStations", List.of("시청"));

        SubwayStationResDTO secondStation = new SubwayStationResDTO();
        ReflectionTestUtils.setField(secondStation, "stationName", "시청");

        given(subwayNetworkService.getStationsBetween(anyString(), anyString(), anyString()))
                .willReturn(List.of(startStation, secondStation));

        // when: 서비스 메서드 실행
        RouteListResDTO result = routeCommandService.searchRoute(request);

        // then: 결과 검증
        TransitRouteDetailInfoResDTO transitDetail = result.getRouteApiResDtoList().getFirst()
                .getRouteDetailInfoResDTOList().getFirst().getTransitDetail();

        assertThat(transitDetail.getUpNext()).isEqualTo("상행 시청");
        assertThat(transitDetail.getDownNext()).isNull();
    }

    private GoogleDirectionApiResponse createMockGoogleResponse(String lineName, String startStop, String endStop) {
        GoogleDirectionApiResponse.EncodedLine line = new GoogleDirectionApiResponse.EncodedLine();
        ReflectionTestUtils.setField(line, "name", lineName);

        GoogleDirectionApiResponse.Vehicle vehicle = new GoogleDirectionApiResponse.Vehicle();
        ReflectionTestUtils.setField(vehicle, "type", "SUBWAY");
        ReflectionTestUtils.setField(line, "vehicle", vehicle);

        GoogleDirectionApiResponse.TransitDetails transitDetails = new GoogleDirectionApiResponse.TransitDetails();
        ReflectionTestUtils.setField(transitDetails, "encodedLine", line);

        GoogleDirectionApiResponse.Step step = new GoogleDirectionApiResponse.Step();
        ReflectionTestUtils.setField(step, "travelMode", "TRANSIT");
        ReflectionTestUtils.setField(step, "transitDetails", transitDetails);

        GoogleDirectionApiResponse.Leg leg = new GoogleDirectionApiResponse.Leg();
        ReflectionTestUtils.setField(leg, "steps", List.of(step));

        GoogleDirectionApiResponse.Route route = new GoogleDirectionApiResponse.Route();
        ReflectionTestUtils.setField(route, "legs", List.of(leg));

        GoogleDirectionApiResponse response = new GoogleDirectionApiResponse();
        ReflectionTestUtils.setField(response, "routes", List.of(route));

        GoogleDirectionApiResponse.DepartureStop departureStop = new GoogleDirectionApiResponse.DepartureStop();
        ReflectionTestUtils.setField(departureStop, "encodedName", startStop);
        ReflectionTestUtils.setField(transitDetails, "departureStop", departureStop);

        GoogleDirectionApiResponse.ArrivalStop arrivalStop = new GoogleDirectionApiResponse.ArrivalStop();
        ReflectionTestUtils.setField(arrivalStop, "encodedName", endStop);
        ReflectionTestUtils.setField(transitDetails, "arrivalStop", arrivalStop);

        return response;
    }
}
