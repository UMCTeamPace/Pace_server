package com.example.pace.domain.transit.controller.docs;

import com.example.pace.domain.transit.dto.request.BusInfoReqDTO;
import com.example.pace.domain.transit.dto.request.SubwayArrivalReqDTO;
import com.example.pace.domain.transit.dto.response.BusInfoResDTO;
import com.example.pace.domain.transit.dto.response.SubwayArrivalResDTO.SubwayArrivalInfoDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Transit", description = "대중교통(지하철/버스) 관련 API")
public interface TransitControllerDocs {

    @Operation(
            summary = "실시간 지하철 도착 정보 조회",
            description = "출발역과 목적지역 정보를 바탕으로 가고자 하는 방향의 실시간 열차 도착 정보를 필터링하여 제공합니다. " +
                    "단순히 몇 분 후 도착 정보뿐만 아니라, 우리 노선 데이터를 활용해 목적지까지 남은 정거장 수(beforeSubwayCount)를 계산해서 알려줍니다."
    )
    ApiResponse<List<SubwayArrivalInfoDTO>> getArrivals(
            SubwayArrivalReqDTO.SubwayArrivalDTO request
    );

    @Operation(
            summary = "버스 실시간 도착 정보 조회를 위한 출발 정류장 식별자 조회",
            description = "버스 노선 번호, 출발 정류장 이름, 목적지 정류장 이름을 입력받아 사용자가 탑승해야 할 정확한 방향의 출발 정류장 노드 ID(nodeId), 노선 ID(routeId), 정류장 순번(sequence)을 반환합니다. 이 정보는 공공데이터 포털 등 실시간 버스 도착 정보 API를 호출하는 데 사용됩니다."
    )
    ApiResponse<BusInfoResDTO.BusInfoDTO> searchBusStartStation(
            BusInfoReqDTO.BusStartStationDTO request
    );
}
