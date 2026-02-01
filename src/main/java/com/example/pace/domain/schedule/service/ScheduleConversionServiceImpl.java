package com.example.pace.domain.schedule.service;

import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.repository.RouteRepository;
import com.example.pace.domain.schedule.converter.ScheduleConversionConverter;
import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.ScheduleErrorCode;
import com.example.pace.domain.schedule.exception.ScheduleException;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleConversionServiceImpl implements ScheduleConversionService {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;

    @Override
    public ScheduleConversionResDto convertPathScheduleToNormal(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getIsPathIncluded()) {
            throw new ScheduleException(ScheduleErrorCode.NOT_PATH_SCHEDULE);
        }

        // 연결된 route 조회 + hard delete
        Route route = routeRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.ROUTE_NOT_FOUND));

        // 일정 상태 전환
        schedule.updateScheduleRoute(false);

        // route hard delete
        routeRepository.delete(route);

        return new ScheduleConversionResDto(schedule.getId(), schedule.getIsPathIncluded());
    }
}
