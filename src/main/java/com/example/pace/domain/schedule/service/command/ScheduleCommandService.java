package com.example.pace.domain.schedule.service.command;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.schedule.converter.ScheduleReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleResDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.enums.UpdateScope;
import com.example.pace.domain.schedule.exception.code.ScheduleErrorCode;
import com.example.pace.domain.schedule.logic.RepeatCalculator;
import com.example.pace.domain.schedule.logic.ScheduleFactory;
import com.example.pace.domain.schedule.repository.RepeatRuleRepository;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import com.example.pace.global.apiPayload.code.GeneralErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.Objects;
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
    private final RepeatRuleRepository repeatRuleRepository;
    private final RepeatCalculator repeatCalculator;
    private final ScheduleFactory scheduleFactory;


    // 일정 생성
    @Transactional
    public ScheduleResDto createSchedule(Long memberId, ScheduleReqDto request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new
                GeneralException(GeneralErrorCode.NOT_FOUND));
        if (Boolean.TRUE.equals(request.getIsPathIncluded()) && Boolean.TRUE.equals(request.getIsRepeat())) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_CANNOT_REPEAT_WITH_PATH);
        }

        // 반복 그룹 정보 생성
        String groupId = Boolean.TRUE.equals(request.getIsRepeat()) ? UUID.randomUUID().toString() : null;
        RepeatRule repeatRule = (groupId != null) ?
                repeatRuleRepository.save(ScheduleReqDtoConverter.toRepeatRule(request.getRepeatInfo())) : null;

        long durationDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        List<LocalDate> dates;
        if (Boolean.TRUE.equals(request.getIsRepeat()) && request.getRepeatInfo() != null) {
            dates = repeatCalculator.calculateDates(request.getRepeatInfo(), request.getStartDate());
        } else {
            dates = List.of(request.getStartDate());
        }

        List<Schedule> scheduleList = dates.stream()
                .map(date -> scheduleFactory.create(
                        member,
                        date,
                        date.plusDays(durationDays),
                        request,
                        repeatRule,
                        groupId))
                .toList();

        List<Schedule> savedSchedules = scheduleRepository.saveAll(scheduleList);

        if (savedSchedules.isEmpty()) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }

        return ScheduleResDtoConverter.toScheduleResDto(savedSchedules.get(0));
    }


    // 일정 상세 조회
    @Transactional(readOnly = true)
    public ScheduleResDto getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, scheduleId)
                .orElseThrow(() -> new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return ScheduleResDtoConverter.toScheduleResDto(schedule);
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
        Long cursorId = (lastId != null) ? lastId : 0L;
        Pageable pageable = PageRequest.of(0, 20);
        Slice<Schedule> schedules = scheduleRepository.findAllByMemberAndDateRange(memberId, cursorDate, cursorId,
                maxSearchDate, pageable);

        return schedules.map(ScheduleResDtoConverter::toScheduleResDto);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedules(Long memberId, List<Long> scheduleIds) {
        if (scheduleIds == null || scheduleIds.isEmpty()) {
            return;
        }
        List<Schedule> schedules = scheduleRepository.findAllWithMemberByIdIn(scheduleIds, memberId);

        if (schedules.size() != scheduleIds.size()) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteAll(schedules);
    }

    // 일정 수정
    @Transactional
    public ScheduleResDto updateSchedule(Long memberId, Long scheduleId, ScheduleUpdateReqDto request,
                                         UpdateScope scope) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, scheduleId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
        if (Boolean.FALSE.equals(request.getIsAllDay())) {
            validateTimeRange(request.getStartTime(), request.getEndTime());
        }
        if (UpdateScope.ALL.equals(scope) && schedule.getRepeatGroupId() != null) {
            // 반복 규칙 자체가 바뀐 경우 -> 기존 그룹 삭제 후 재생성
            if (request.getRepeatInfo() != null && isRepeatRuleChanged(schedule.getRepeatRule(),
                    request.getRepeatInfo())) {
                LocalDate currentStart =
                        (request.getStartDate() != null) ? request.getStartDate() : schedule.getStartDate();
                LocalDate currentEnd = (request.getEndDate() != null) ? request.getEndDate() : schedule.getEndDate();
                long durationDays = ChronoUnit.DAYS.between(currentStart, currentEnd);

                scheduleRepository.deleteAllByRepeatGroupId(schedule.getRepeatGroupId());

                RepeatRule newRule = repeatRuleRepository.save(
                        ScheduleReqDtoConverter.toRepeatRule(request.getRepeatInfo()));

                // 날짜 계산
                List<LocalDate> newDates = repeatCalculator.calculateDates(request.getRepeatInfo(), currentStart);

                // 새 일정 생성
                List<Schedule> newSchedules = newDates.stream()
                        .map(date -> scheduleFactory.createFromUpdate(
                                schedule,
                                date,
                                date.plusDays(durationDays),
                                request,
                                newRule, // 새로 저장한 규칙 전달
                                schedule.getRepeatGroupId()
                        ))
                        .toList();
                List<Schedule> savedSchedules = scheduleRepository.saveAll(newSchedules);

                return ScheduleResDtoConverter.toScheduleResDto(savedSchedules.get(0));
            }
            // 내용만 바뀐 경우
            else {
                List<Schedule> groupSchedules = scheduleRepository.findAllByRepeatGroupId(schedule.getRepeatGroupId());
                groupSchedules.forEach(s -> s.updateDetailedInfo(request));
            }
        }
        // 단일 수정
        else {
            schedule.updateDetailedInfo(request);
        }

        return ScheduleResDtoConverter.toScheduleResDto(schedule);

    }

    // 시간 검증
    private void validateTimeRange(LocalTime start, LocalTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new GeneralException(ScheduleErrorCode.INVALID_TIME_RANGE);
        }
    }

    private boolean isRepeatRuleChanged(RepeatRule existing, ScheduleReqDto.RepeatDto requestInfo) {
        if (requestInfo == null || existing == null) {
            return false;
        }

        return !existing.getRepeatType().equals(requestInfo.getRepeatType()) ||
                !existing.getRepeatInterval().equals(requestInfo.getRepeatInterval()) ||
                !Objects.equals(existing.getDaysOfWeek(), requestInfo.getDaysOfWeek()) ||
                !existing.getEndType().equals(requestInfo.getEndType()) ||
                !Objects.equals(existing.getEndCount(), requestInfo.getEndCount()) ||
                !Objects.equals(existing.getEndDate(), requestInfo.getRepeatEndDate());
    }
}