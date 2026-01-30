package com.example.pace.domain.transit.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.example.pace.domain.transit.dto.SubwayStationDTO;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
public class SubwayNetworkServiceTest {
    @Autowired
    private SubwayNetworkService subwayNetworkService;

    @Test
    @DisplayName("2호선 강남역에서 삼성역까지의 중간역들이 순서대로 반환되는지 확인")
    void getStationsBetween_Line2_Success() {
        String line = "2호선";
        String start = "강남";
        String end = "삼성";

        List<SubwayStationDTO> path = subwayNetworkService.getStationsBetween(line, start, end);

        assertThat(path).isNotEmpty();

        List<String> stationNames = path.stream()
                .map(SubwayStationDTO::getStationName)
                .toList();

        System.out.println("탐색된 경로: " + stationNames);

        assertThat(stationNames).containsExactly("강남", "역삼", "선릉", "삼성");
    }

    @Test
    @DisplayName("2호선 외선 순환 구간이 잘 연결되어 있는지 확인")
    void getStationsBetween_Line1_Extension() {
        String line = "2호선";
        String start = "성수";
        String end = "용두";

        List<SubwayStationDTO> path = subwayNetworkService.getStationsBetween(line, start, end);

        List<String> stationNames = path.stream()
                .map(SubwayStationDTO::getStationName)
                .toList();

        System.out.println("2호선 외선 순환 경로: " + stationNames);

        assertThat(stationNames).containsExactly("성수", "용답", "신답", "용두");
    }
}
