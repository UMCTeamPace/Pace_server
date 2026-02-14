package com.example.pace.domain.schedule.service.command;

import com.example.pace.domain.schedule.dto.response.ScheduleRouteDeleteResDto;
import com.example.pace.domain.schedule.entity.Route;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.code.ScheduleErrorCode;
import com.example.pace.domain.schedule.exception.ScheduleException;
import com.example.pace.domain.schedule.repository.RouteRepository;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleRouteDeleteCommandService {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public ScheduleRouteDeleteResDto deleteScheduleRoute(Long scheduleId, Long memberId) {

        // 내 일정인지(scheduleId + memberId)로 조회
        Schedule schedule = scheduleRepository.findByIdAndMemberId(scheduleId, memberId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // 일정에 연결된 Route 조회
        Route route = routeRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new ScheduleException(ScheduleErrorCode.ROUTE_NOT_FOUND));

        // Schedule - Route 연관관계 끊기
        schedule.setRoute(null);

        // 실제 Route 삭제 (RouteDetail은 cascade+orphanRemoval로 같이 삭제되는 구조가 일반적)
        routeRepository.delete(route);

        //일반 일정으로 진행
        schedule.setIsPathIncluded(false);

        // 응답용 (updatedAt은 auditing으로 갱신되거나, 필요시 scheduleRepository.save(schedule) 해도 됨)
        return ScheduleRouteDeleteResDto.of(schedule.getId(), schedule.getUpdatedAt());
    }
}
