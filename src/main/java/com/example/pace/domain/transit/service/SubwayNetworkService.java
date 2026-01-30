package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.dto.SubwayStationDTO;
import com.example.pace.domain.transit.loader.SubwayDataLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubwayNetworkService {
    private final SubwayDataLoader subwayDataLoader;

    public List<SubwayStationDTO> getStationsBetween(String lineName, String startName, String endName) {
        String startKey = makeKey(lineName, startName);
        String endKey = makeKey(lineName, endName);

        if (subwayDataLoader.getStation(lineName, startName) == null ||
                subwayDataLoader.getStation(lineName, endName) == null
        ) {
            log.warn("역 정보를 찾을 수 없습니다: {} {} -> {}", lineName, startName, endName);
            return Collections.emptyList();
        }

        if (startKey.equals(endKey)) {
            // 단일 요소를 가진 불변 리스트 반환(중간역이 없으므로)
            return Collections.singletonList(subwayDataLoader.getStation(lineName, startName));
        }

        // bfs 탐색
        Queue<String> queue = new LinkedList<>();
        queue.add(startKey);

        Map<String, String> cameFrom = new HashMap<>();
        cameFrom.put(startKey, null);

        while (!queue.isEmpty()) {
            String currentKey = queue.poll();

            if (currentKey.equals(endKey)) {
                return reconstructPath(cameFrom, endKey);
            }

            SubwayStationDTO currentStation = getStationByKey(currentKey);

            if (currentStation == null) {
                continue;
            }

            // 같은 호선의 이웃 역 확인
            List<String> neighborList = getSameLineNeighbors(currentStation);

            for (String neighborKey : neighborList) {
                if (!cameFrom.containsKey(neighborKey)) {
                    cameFrom.put(neighborKey, currentKey);
                    queue.add(neighborKey);
                }
            }
        }

        log.error("같은 호선 내 경로 탐색 실패: {} -> {}", startName, endName);

        return Collections.emptyList();
    }

    private List<String> getSameLineNeighbors(SubwayStationDTO station) {
        List<String> neighborList = new LinkedList<>();
        String line = station.getLineName();

        if (station.getNextStations() != null) {
            for (String next : station.getNextStations()) {
                neighborList.add(makeKey(line, next));
            }
        }

        if (station.getPrevStations() != null) {
            for (String prev : station.getPrevStations()) {
                neighborList.add(makeKey(line, prev));
            }
        }

        return neighborList;
    }

    private List<SubwayStationDTO> reconstructPath(Map<String, String> cameFrom, String endKey) {
        LinkedList<SubwayStationDTO> path = new LinkedList<>();
        String currentKey = endKey;

        while (currentKey != null) {
            path.addFirst(getStationByKey(currentKey));
            currentKey = cameFrom.get(currentKey);
        }

        return path;
    }

    private SubwayStationDTO getStationByKey(String key) {
        String[] parts = key.split("_");
        return subwayDataLoader.getStation(parts[0], parts[1]);
    }

    private String makeKey(String lineName, String stationName) {
        return lineName + "_" + stationName;
    }
}
