package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.dto.request.SubwayArrivalReqDTO;
import com.example.pace.domain.transit.dto.response.SubwayArrivalResDTO.SubwayArrivalInfoDTO;
import com.example.pace.domain.transit.service.query.SubwayApiQueryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SubwayApiQueryServiceTest {
    @Autowired
    private SubwayApiQueryService subwayApiQueryService;

    @Test
    @DisplayName("실제 실시간 지하철 도착 정보 API를 호출하여 정상적으로 데이터가 오는지 확인함")
    void getLiveSubway_Success() {
        SubwayArrivalReqDTO.SubwayArrivalDTO request = new SubwayArrivalReqDTO.SubwayArrivalDTO(
                "건대입구",
                "태릉입구",
                "7호선"
        );

        List<SubwayArrivalInfoDTO> result = subwayApiQueryService.getLiveSubwayStation(request);

        // 응답 리스트가 null이 아니어야 함 (열차가 존재하지 않아도)
        assertThat(result).isNotNull();

        // 결과가 있다면 각 필드 값들이 제대로 들어있는지 출력
        if (result.isEmpty()) {
            System.out.println("현재 운행 중인 열차가 없습니다.");
        } else {
            System.out.println("데이터 수신 성공! (검색된 열차: " + result.size() + "대)");

            result.forEach(info -> {
                System.out.println("-----------------");
                System.out.println("도착지 방면: " + info.getTrainLineNm());
                System.out.println("다음 열차 도착 예정 시간: " + info.getBarvlDt() + "초");
                System.out.println("도착 메세지: " + info.getArvlMsg2());
                System.out.println("종착역: " + info.getBstatnNm());
                System.out.println("상/하행: " + info.getUpdnLine());
                System.out.println("열차가 어디에 있는지: " + info.getArvlMsg3());
                System.out.println("몇 전역에 있는지: " + info.getBeforeSubwayCount());
                // 필터링이 잘 되었는지 확인
                // 모든 결과는 7호선(1007)이어야 함
                assertThat(info.getSubwayId()).isEqualTo("1007");
                // 모든 결과는 목적지인 방면(상행)이어야 함
                assertThat(info.getUpdnLine()).isEqualTo("상행");
            });
        }
    }

}
