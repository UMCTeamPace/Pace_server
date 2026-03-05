package com.example.pace.domain.schedule.service.command;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.code.ScheduleErrorCode;
import com.example.pace.domain.schedule.logic.ScheduleFactory;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import com.example.pace.global.apiPayload.code.GeneralErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;
import java.time.LocalTime;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleCommandService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ScheduleFactory scheduleFactory;
    private final ScheduleConverter scheduleConverter;


    // 일정 생성
    @Transactional
    public ScheduleResDto createSchedule(Long memberId, ScheduleReqDto request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new
                GeneralException(GeneralErrorCode.NOT_FOUND));


        Schedule schedule = scheduleFactory.create(
                member,
                request.getStartDate(),
                request.getEndDate(),
                request
        );
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleConverter.toScheduleResDto(savedSchedule);
    }


    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ScheduleResDto getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, Math.abs(scheduleId))
                .orElseThrow(() -> new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return scheduleConverter.toScheduleResDto(schedule);
    }

    @Transactional(readOnly = true)
    public Slice<ScheduleResDto> getScheduleList(
            Long memberId,
            LocalDate startDate,
            LocalDate maxSearchDate,
            LocalDate lastDate,
            Long lastId
    ) {
        LocalDate cursorDate = (lastDate != null) ? lastDate : startDate;
        Long cursorId = (lastId != null) ? Math.abs(lastId) : 0L;
        Pageable pageable = PageRequest.of(0, 20);
        Slice<Schedule> schedules = scheduleRepository.findAllByMemberAndDateRange(memberId, cursorDate, cursorId,
                maxSearchDate, pageable);

        return schedules.map(scheduleConverter::toScheduleResDto);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedules(Long memberId, List<Long> scheduleIds) {
        if (scheduleIds == null || scheduleIds.isEmpty()) return;
        List<Long> originalIds = scheduleIds.stream()
                .map(Math::abs)
                .toList();
        List<Schedule> schedules = scheduleRepository.findAllWithMemberByIdIn(originalIds, memberId);

        if (schedules.size() != scheduleIds.size()) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteAll(schedules);
    }

    // 일정 수정
    @Transactional
    public ScheduleResDto updateSchedule(Long memberId, Long scheduleId, ScheduleUpdateReqDto request) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, Math.abs(scheduleId))
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
        if (request.getStartTime() != null || request.getEndTime() != null)  {
            validateTimeRange(request.getStartTime(), request.getEndTime());
        }
        applyScheduleUpdate(schedule, request);
        return scheduleConverter.toScheduleResDto(schedule);

    }

    private void applyScheduleUpdate(Schedule schedule, ScheduleUpdateReqDto dto) {
        // 기본 필드 업데이트
        if (dto.getTitle() != null) schedule.setTitle(dto.getTitle());
        if (dto.getMemo() != null) schedule.setMemo(dto.getMemo());
        if (dto.getStartDate() != null) schedule.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) schedule.setEndDate(dto.getEndDate());
        if (dto.getStartTime() != null) schedule.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) schedule.setEndTime(dto.getEndTime());
        if (dto.getCalendarId() != null) schedule.setCalendarId(dto.getCalendarId());
        if (dto.getColor() != null) schedule.setColor(dto.getColor());
        if (dto.getIsPathIncluded() != null) schedule.setIsPathIncluded(dto.getIsPathIncluded());

        // 장소 업데이트
        if (dto.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(dto.getPlace()));
        }

        // 알림 업데이트
        if (dto.getReminders() != null) {
            schedule.getReminderList().clear();
            dto.getReminders().forEach(reminderDto ->
                    schedule.addReminder(ReminderReqDtoConverter.toReminder(reminderDto))
            );
        }
    }
    // 시간 검증
    private void validateTimeRange(LocalTime start, LocalTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new GeneralException(ScheduleErrorCode.INVALID_TIME_RANGE);
        }
    }
}