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
    public ScheduleRouteUpdateResDto updateScheduleRoute(
            Long memberId,
            Long scheduleId,
            ScheduleRouteUpdateReqDto req
    ) {
        // memberId AND 조건으로 조회
        Schedule schedule = scheduleRepository.findByIdAndMemberId(scheduleId, memberId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND.getMessage()));

        // 기존 route 제거 (기존 route + detail 자동)
        if (schedule.getRoute() != null) {
            schedule.setRoute(null);
        }

        // 새 route 생성
        Route newRoute = ScheduleRouteUpdateReqDtoConverter.toRoute(req, schedule);

        // route details 생성 + route랑 연결
        List<RouteDetail> details = ScheduleRouteUpdateReqDtoConverter.toRouteDetails(newRoute, req.getRoute());
        newRoute.getRouteDetails().addAll(details);

        // schedule에 연결
        schedule.setRoute(newRoute);

        // 저장
        routeRepository.save(newRoute);

        return ScheduleRouteUpdateResDto.from(schedule);
    }
}
