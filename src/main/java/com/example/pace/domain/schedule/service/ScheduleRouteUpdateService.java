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
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow();

        if (!schedule.getMember().getId().equals(memberId)) {
            throw new ScheduleException(ScheduleErrorCode.SCHEDULE_FORBIDDEN.getMessage());
        }
        // 0) 기존 route 제거
        Route oldRoute = schedule.getRoute();
        if (oldRoute != null) {
            schedule.setRoute(null);          // 연관 끊고
            routeRepository.delete(oldRoute); // 기존 route 삭제
            routeRepository.flush();          // 즉시 DB 반영
        }

        // 1) Route 변환
        Route newRoute = ScheduleRouteUpdateReqDtoConverter.toRoute(req);

        // 2) Schedule - Route 연결
        schedule.addRoute(newRoute);

        // 3) RouteDetail 변환/연결
        if (req.getRouteDetails() != null) {
            for (ScheduleRouteUpdateReqDto.RouteDetailDto dto : req.getRouteDetails()) {
                RouteDetail detail =
                        ScheduleRouteUpdateReqDtoConverter.toRouteDetail(dto);
                newRoute.addRouteDetail(detail);
            }
        }

        scheduleRepository.save(schedule);

        return ScheduleRouteUpdateResDto.from(schedule);
    }

}
