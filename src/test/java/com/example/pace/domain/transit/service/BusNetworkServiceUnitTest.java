package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.dto.response.BusInfoResDTO;
import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.exception.TransitException;
import com.example.pace.domain.transit.exception.code.TransitErrorCode;
import com.example.pace.domain.transit.repository.BusInfoRepository;
import com.example.pace.domain.transit.repository.BusInfoRepositoryCustom.BusRoutePair;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

// 해당 클래스에서 Mockito를 사용하겠다고 JUnit에게 알려주는 역할
@ExtendWith(MockitoExtension.class)
public class BusNetworkServiceUnitTest {
    // 스프링이 빈을 주입하듯이 Mockito가 아래에서 만든 @Mock 가짜 객체들을 해당 클래스 안에 inject함
    @InjectMocks
    private BusNetworkService busNetworkService;

    // 가짜 객체 만들기
    // 서비스 로직만 테스트하고 싶은데, 실제 DB를 붙이면 너무 느리고 복잡해지기 때문
    @Mock
    private BusInfoRepository busInfoRepository;

    @Test
    @DisplayName("버스 정보 조회 성공: 올바른 방향의 정류장과 종점 방면을 찾는다.")
    void searchStartStationInfo_Success() {
        // given 단계
        String lineName = "1014";
        String startStationName = "정릉시장입구";
        String endStationName = "신설동역오거리";

        // 데이터베이스에서 꺼내온 척하는 엔티티를 하나 만듬
        BusInfo startStation = BusInfo.builder()
                .lineName(lineName)
                .stationName(startStationName)
                .nodeId("107000077")
                .busRouteId("100100129")
                .arsId("08167")
                .sequence(7)
                .stationLat(BigDecimal.valueOf(37.6084653300))
                .stationLng(BigDecimal.valueOf(127.0098213000))
                .build();

        // mock 객체에게 findCorrectBusRoute를 호출하면 BusRoutePair 객체를 반환하라는 의미
        given(busInfoRepository.findCorrectBusRoute(
                lineName,
                startStationName,
                endStationName
        )).willReturn(Optional.of(
                new BusRoutePair(
                        startStation,
                        null
                )
        ));

        // when 단계
        BusInfoResDTO.BusInfoDTO response = busNetworkService.searchStartStationInfo(
                lineName,
                startStationName,
                endStationName
        );

        assertThat(response.getSequence()).isEqualTo(startStation.getSequence());
        assertThat(response.getNodeId()).isEqualTo(startStation.getNodeId());
    }

    @Test
    @DisplayName("실패: 올바른 경로를 찾지 못하면 예외가 발생")
    void searchStartStationInfo_Failure() {
        // given
        // 조회 쿼리가 실행되었을 때 데이터를 못찾아서 빈 객체를 반환한다고 가정
        given(busInfoRepository.findCorrectBusRoute(anyString(), anyString(), anyString()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> busNetworkService.searchStartStationInfo(
                "5432",
                "없는역!",
                "모르는역!"
        ))
                .isInstanceOf(TransitException.class)
                .hasMessageContaining(TransitErrorCode.TRANSIT_BUS_NOT_FOUND.getMessage());
    }
}














