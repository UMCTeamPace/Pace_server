package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.Route;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

    //schedule로 route찾기 위해
    Optional<Route> findByScheduleId(Long scheduleId);
    void deleteByScheduleId(Long scheduleId);
}
