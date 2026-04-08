package com.example.pace.domain.transit.repository;

import com.example.pace.domain.transit.entity.BusInfo;
import java.util.Optional;

public interface BusInfoRepositoryCustom {
    record BusRoutePair(BusInfo startStation, BusInfo endStation) {
    }

    // 버스 번호, 탑승지, 하차지를 받아 올바른 방향의 탑승 정류장 정보를 반환
    Optional<BusRoutePair> findCorrectBusRoute(String lineName, String boardingStopName, String alightingStopName);
}
