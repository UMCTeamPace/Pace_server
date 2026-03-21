package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.dto.response.BusInfoResDTO;
import com.example.pace.domain.transit.dto.response.BusInfoResDTO.BusInfoDTO;
import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.exception.TransitException;
import com.example.pace.domain.transit.exception.code.TransitErrorCode;
import com.example.pace.domain.transit.repository.BusInfoRepository;
import com.example.pace.domain.transit.repository.BusInfoRepositoryCustom.BusRoutePair;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class BusNetworkService {
    private final BusInfoRepository busInfoRepository;

    // 실시간 도착 정보 조회를 위한 단일 정류장 정보 반환
    @Transactional(readOnly = true)
    public BusInfoResDTO.BusInfoDTO searchStartStationInfo(
            String lineName,
            String startStation,
            String endStation
    ) {
        BusInfo startStop = busInfoRepository.findCorrectBusRoute(
                        lineName,
                        startStation,
                        endStation
                )
                .orElseThrow(() -> new TransitException(TransitErrorCode.TRANSIT_BUS_NOT_FOUND))
                .startStation();

        return BusInfoResDTO.BusInfoDTO.builder()
                .nodeId(startStop.getNodeId())
                .routeId(startStop.getBusRouteId())
                .sequence(startStop.getSequence())
                .build();
    }

    // 출발지부터 도착지 사이의 모든 정류장 목록을 반환
    @Transactional(readOnly = true)
    public List<BusInfo> getStationsBetween(String lineName, String startStation, String endStation) {
        BusRoutePair routePair = busInfoRepository.findCorrectBusRoute(
                        lineName,
                        startStation,
                        endStation
                )
                .orElseThrow(() -> new TransitException(TransitErrorCode.TRANSIT_BUS_NOT_FOUND));

        return busInfoRepository.findIntermediateStops(
                lineName,
                routePair.startStation().getSequence(),
                routePair.endStation().getSequence()
        );
    }
}
