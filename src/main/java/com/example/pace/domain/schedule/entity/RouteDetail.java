package com.example.pace.domain.schedule.entity;


import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(
        name = "route_detail",
        indexes = {
                // 특정 경로의 상세 정보를 순서대로 조회할 때 사용하기 위한 인덱스 칼럼 지정
                @Index(name = "idx_route_detail_route_sequence", columnList = "route_id, sequence")
        }
)
public class RouteDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "sequence")
    private Integer sequence; //경로 내 순서

    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    public void setRoute(Route route) {
        this.route = route;
    }
}