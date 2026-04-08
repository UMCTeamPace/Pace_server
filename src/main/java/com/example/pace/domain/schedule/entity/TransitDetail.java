package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransitDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransitType transitType; // BUS, SUBWAY 등

    private String lineName;
    private String lineColor;
    private Integer stopCount;
    private String departureStop;
    private String arrivalStop;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String shortName;
    private Double locationLat;
    private Double locationLng;
    private String headsign;

    @ElementCollection
    @CollectionTable(name = "transit_station_path", joinColumns = @JoinColumn(name = "transit_detail_id"))
    @Column(name = "station_name")
    private List<String> stationPath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_detail_id", unique = true)
    private RouteDetail routeDetail;

}
