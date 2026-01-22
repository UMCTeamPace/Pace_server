package com.example.pace.domain.transit.service;

import com.example.pace.domain.transit.vo.SubwayGraphNode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SubwayNetworkService {
    // key: 호선_역이름(ex: "1호선_신도림", "2호선_신도림")
    private final Map<String, SubwayGraphNode> stationMap = new HashMap<>();

    public void addStation(SubwayGraphNode node) {
        String key = node.getLineName() + "_" + node.getStationName();
        stationMap.put(key, node);
    }

    public SubwayGraphNode getStation(String lineName, String stationName) {
        return stationMap.get(lineName + "_" + stationName);
    }

    // 추후에 중간역 찾기 로직(bfs 등)을 구현할 예정
}
