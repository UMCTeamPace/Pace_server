package com.example.pace.domain.transit.repository;

import com.example.pace.domain.transit.entity.BusInfo;
import com.example.pace.domain.transit.entity.QBusInfo;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BusInfoRepositoryCustomImpl implements BusInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BusRoutePair> findCorrectBusRoute(
            String lineName,
            String startStationName,
            String endStationName
    ) {
        QBusInfo boarding = new QBusInfo("boarding"); // 탑승 정류장
        QBusInfo alighting = new QBusInfo("alighting"); // 하차 정류장

        Tuple result = queryFactory
                .select(boarding, alighting)
                .from(boarding)
                .join(alighting).on(boarding.busRouteId.eq(alighting.busRouteId))
                .where(
                        boarding.lineName.eq(lineName),
                        boarding.stationName.eq(startStationName),
                        alighting.stationName.eq(endStationName),
                        // 출발 정류장의 순번이 하차 정류장의 순서보다 작아야 올바른 방향임
                        boarding.sequence.lt(alighting.sequence)
                )
                // 탑승지와 하차지 사이의 정류장 개수 차이가 가장 적은 경로를 우선적으로 정렬
                .orderBy(alighting.sequence.subtract(boarding.sequence).asc())
                .fetchFirst();

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(
                new BusRoutePair(
                        result.get(boarding),
                        result.get(alighting)
                )
        );
    }
}
