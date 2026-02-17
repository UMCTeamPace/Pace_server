package com.example.pace.domain.transit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "bus_info",
        indexes = {
                @Index(name = "idx_bus_line_name", columnList = "line_name"), // 노선 번호로 검색 최적화
                @Index(name = "idx_bus_line_sequence", columnList = "line_name, sequence") // 노선 내 순서 정렬 최적화
        }
)
public class BusInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_route_id", nullable = false)
    private String busRouteId; // 노선 ID

    @Column(name = "line_name", nullable = false)
    private String lineName; // 노선 명 (버스 번호, 예: 150, 143)

    @Column(name = "station_name", nullable = false)
    private String stationName; // 정류소 이름

    @Column(name = "sequence", nullable = false)
    private Integer sequence; // 노선 내 운행 순번

    @Column(name = "ars_id", nullable = false)
    private String arsId; // 정류소 고유 번호 (ARS-ID 아님, 표준 ID)

    // precision: 전체 자릿수(정수부 + 소수부)
    // scale: 소수점 이하 자릿수(10자리까지 저장)
    @Column(name = "station_lat", nullable = false, precision = 18, scale = 10)
    private BigDecimal stationLat; // 위도

    @Column(name = "station_lng", nullable = false, precision = 18, scale = 10)
    private BigDecimal stationLng; // 경도
}