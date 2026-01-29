package com.example.pace.domain.schedule.entity;


import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "route_detail")
public class RouteDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "sequence")
    private Integer sequence; //경로 내 순서

    @Column(name = "start_lat")
    private BigDecimal startLat; //경로 내 시작 장소 위도

    @Column(name = "start_lng")
    private BigDecimal startLng; //경로 내 시작 장소 경도

    @Column(name = "end_lat")
    private BigDecimal endLat; //경로 내 도착 장소 위도

    @Column(name = "end_lng")
    private BigDecimal endLng; //경로 내 도착 장소 경도

    @Column(name = "transit_type")
    @Enumerated(EnumType.STRING)
    private TransitType transitType; //이동수단

    @Column(name = "duration")
    private Integer duration; //해당 구간 소요 시간

    @Column(name = "distance")
    private Integer distance; //해당 구간 이동 거리

    @Column(name = "description")
    private String description; //안내 문구

    @Column(name = "line_name")
    private String lineName; //노선 이름

    @Column(name = "line_color")
    private String lineColor; //노선 색상

    @Column(name = "stop_count")
    private Integer stopCount; //정차역 개수

    @Column(name = "departure_stop")
    private String departureStop; //승차역 정류장 이름

    @Column(name = "arrival_stop")
    private String arrivalStop; //하차역 정류장 이름

    public void setRoute(Route route) {
        this.route = route;
    }
}