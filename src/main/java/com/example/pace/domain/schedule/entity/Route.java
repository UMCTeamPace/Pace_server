package com.example.pace.domain.schedule.entity;


import com.example.pace.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "route")

public class Route extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteDetail> routeDetails = new ArrayList<>();

    @Column(name = "origin_name")
    private String originName; //출발지 이름

    @Column(name = "dest_name")
    private String destName; //도착지 이름

    @Column(name = "origin_lat")
    private BigDecimal originLat; //출발지 위도

    @Column(name = "origin_lng")
    private BigDecimal originLng; //출발지 경도

    @Column(name = "dest_lat")
    private BigDecimal destLat; //목적지 위도

    @Column(name = "dest_lng")
    private BigDecimal destLng; //목적지 경도

    @Column(name = "total_time")
    private int totalTime; //총 소요 시간

    @Column(name = "total_distance")
    private int totalDistance; //총 이동 거리(m)

    @Column(name = "is_saved")
    private boolean isSaved; //저장 여부

    //Fk,연관관계 추후 진행
}