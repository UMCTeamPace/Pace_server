package com.example.pace.domain.transit.loader;

import com.example.pace.domain.transit.dto.SubwayStationDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubwayDataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, SubwayStationDTO> stationMap = new HashMap<>();

    @Override
    public void run(@NotNull String... args) throws Exception {
        log.info("지하철 데이터 불러오는 중...");
        loadSubwayDataFromJson();
    }

    private void loadSubwayDataFromJson() {
        String SUBWAY_JSON_PATH = "data/subway_final_data.json";
        ClassPathResource resource = new ClassPathResource(SUBWAY_JSON_PATH);

        if (!resource.exists()) {
            log.warn("해당 json 파일을 찾지 못했습니다.");
            return;
        }

        try (InputStream fis = resource.getInputStream()) {
            List<SubwayStationDTO> stationList = objectMapper.readValue(
                    fis,
                    new TypeReference<>() {
                    }
            );

            // 파싱된 데이터를 Map에 적재
            for (SubwayStationDTO station : stationList) {
                String key = makeKey(station.getLineName(), station.getStationName());
                stationMap.put(key, station);
            }

            log.info("지하철 데이터 {}개를 로드하였습니다.", stationMap.size());
        } catch (IOException e) {
            log.error("지하철 데이터 파싱 에러: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String makeKey(String lineName, String stationName) {
        return lineName + "_" + stationName;
    }

    // 호선명과 역 이름으로 특정 역 정보 가져오기
    public SubwayStationDTO getStation(String lineName, String stationName) {
        return stationMap.get(makeKey(lineName, stationName));
    }

    public Map<String, SubwayStationDTO> getAllStations() {
        return Collections.unmodifiableMap(stationMap);
    }
}