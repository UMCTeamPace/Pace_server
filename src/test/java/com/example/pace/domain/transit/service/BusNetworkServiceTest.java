package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.repository.BusInfoRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2로 교체하지 않고 실제 DB 사용
class BusNetworkServiceTest {
    @Autowired
    private BusNetworkService busNetworkService;

    @Autowired
    private BusInfoRepository busInfoRepository;

    @Test
    @DisplayName("정방향 경로 조회 테스트")
    void getStationsBetween_Forward() {
        // given
        // 버스 번호 1014를 기준으로 테스트
        String lineName = "1014";

        // 해당 노선의 모든 정류장 가져오기
        List<BusInfo> allStopList = busInfoRepository.findAll()
                .stream().filter(b -> b.getLineName().equals(lineName)).limit(10).toList();

        String start = allStopList.getFirst().getStationName();
        String middle = allStopList.get(1).getStationName();
        String middle2 = allStopList.get(2).getStationName();
        String end = allStopList.get(5).getStationName();

        System.out.println("테스트 대상 노선: " + lineName + ", 출발: " + start + ", 도착: " + end);

        List<BusInfo> result = busNetworkService.getStationsBetween(lineName, start, end);

        assertThat(result).isNotEmpty();

        List<String> names = result.stream().map(BusInfo::getStationName).toList();

        System.out.println("조회된 경로: " + names);

        assertThat(names).contains(start, middle, middle2, end);
    }
}
