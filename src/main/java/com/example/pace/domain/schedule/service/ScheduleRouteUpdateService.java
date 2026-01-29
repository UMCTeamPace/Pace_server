package com.example.pace.domain.schedule.service;

import com.example.pace.domain.schedule.converter.ScheduleRouteUpdateReqDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleRouteUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleRouteUpdateResDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.ScheduleErrorCode;
import com.example.pace.domain.schedule.exception.ScheduleException;
import com.example.pace.domain.schedule.repository.RouteRepository;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleRouteUpdateService {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public ScheduleRouteUpdateResDto updateScheduleRoute(Long memberId, Long scheduleId, ScheduleRouteUpdateReqDto req) {
        Schedule schedule = scheduleRepository.findByIdAndMemberId(scheduleId, memberId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND.getMessage()));

        // 0) 기존 route 제거
        Route oldRoute = schedule.getRoute();
        if (oldRoute != null) {
            schedule.removeRoute();
            routeRepository.delete(oldRoute);
            routeRepository.flush();
        }

        // 1) Route 변환
        Route newRoute = ScheduleRouteUpdateReqDtoConverter.toRoute(req);

        // 2) RouteDetail 변환/연결
        if (req.getRouteDetails() != null) {
            for (ScheduleRouteUpdateReqDto.RouteDetailDto dto : req.getRouteDetails()) {
                RouteDetail detail =
                        ScheduleRouteUpdateReqDtoConverter.toRouteDetail(dto);
                newRoute.addRouteDetail(detail);
            }
        }

        // 3) Schedule - Route 연결
        schedule.addRoute(newRoute);

        scheduleRepository.save(schedule);

        return ScheduleRouteUpdateResDto.from(schedule);
    }

}
