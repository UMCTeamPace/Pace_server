package com.example.pace.domain.transit.repository;

import com.example.pace.domain.transit.entity.BusInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusInfoRepository extends JpaRepository<BusInfo, Long>, BusInfoRepositoryCustom {
    // 특정 노선(lineName)에서 출발 순번과 도착 순번 사이의 정류장들을 조회 (순서대로)
    @Query("SELECT b FROM BusInfo b "
            + "WHERE b.lineName = :lineName AND b.sequence >= :startSeq AND b.sequence <= :endSeq "
            + "ORDER BY b.sequence ASC")
    List<BusInfo> findIntermediateStops(
            @Param("lineName") String lineName,
            @Param("startSeq") Integer startSeq,
            @Param("endSeq") Integer endSeq
    );
}
