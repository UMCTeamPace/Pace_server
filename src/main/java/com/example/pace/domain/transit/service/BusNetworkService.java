package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.repository.BusInfoRepository;
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

    @Transactional(readOnly = true)
    public List<BusInfo> getStationsBetween(String lineName, String startStation, String endStation) {
        List<BusInfo> startStopList = busInfoRepository.findByLineNameAndStationName(lineName, startStation);
        List<BusInfo> endStopList = busInfoRepository.findByLineNameAndStationName(lineName, endStation);

        if (startStopList.isEmpty() || endStopList.isEmpty()) {
            log.warn("정류장 정보를 찾을 수 없습니다.: {} ({} -> {})", lineName, startStation, endStation);
            return Collections.emptyList();
        }

        BusInfo start = startStopList.getFirst();
        BusInfo end = endStopList.getFirst();

        int startSeq = start.getSequence();
        int endSeq = end.getSequence();

        int minSeq = Math.min(startSeq, endSeq);
        int maxSeq = Math.max(startSeq, endSeq);

        List<BusInfo> path = busInfoRepository.findIntermediateStops(lineName, minSeq, maxSeq);

        if (startSeq > endSeq) {
            // 역방향인 경우 순번 내림차순 정렬
            path.sort(Comparator.comparing(BusInfo::getSequence).reversed());
        } else {
            // 정방향인 경우 순번 오름차순 정렬
            path.sort(Comparator.comparing(BusInfo::getSequence));
        }

        return path;
    }
}
