package com.example.pace.domain.schedule.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.schedule.converter.ScheduleReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleResDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.ScheduleErrorCode;
import com.example.pace.domain.schedule.logic.RepeatCalculator;
import com.example.pace.domain.schedule.logic.ScheduleFactory;
import com.example.pace.domain.schedule.repository.RepeatRuleRepository;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import com.example.pace.global.apiPayload.code.GeneralErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final RepeatRuleRepository repeatRuleRepository;
    private final RepeatCalculator repeatCalculator;
    private final ScheduleFactory scheduleFactory;

    @Transactional
    public ScheduleResDto createSchedule(Long memberId, ScheduleReqDto request) {
        Member member = findMember(memberId);
        validateCreateRequest(request);

        // 반복 세팅
        String groupId = isTrue(request.getIsRepeat()) ? UUID.randomUUID().toString() : null;
        RepeatRule repeatRule = (groupId != null && request.getRepeatInfo() != null)
                ? repeatRuleRepository.save(ScheduleReqDtoConverter.toRepeatRule(request.getRepeatInfo()))
                : null;

        // 생성 날짜 목록
        List<LocalDate> dates = (groupId != null && request.getRepeatInfo() != null)
                ? repeatCalculator.calculateDates(request.getRepeatInfo(), request.getStartDate())
                : List.of(request.getStartDate());

        List<Schedule> schedules = dates.stream()
                .map(date -> scheduleFactory.create(member, date, request, repeatRule, groupId))
                .toList();

        List<Schedule> saved = scheduleRepository.saveAll(schedules);
        if (saved.isEmpty()) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }
        return ScheduleResDtoConverter.toScheduleResDto(saved.get(0));
    }

    public ScheduleResDto getSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, scheduleId)
                .orElseThrow(() -> new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        return ScheduleResDtoConverter.toScheduleResDto(schedule);
    }

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
        Slice<Schedule> schedules = scheduleRepository.findAllByMemberAndDateRange(
                memberId, cursorDate, cursorId, maxSearchDate, pageable
        );

        return schedules.map(ScheduleResDtoConverter::toScheduleResDto);
    }

    @Transactional
    public void deleteSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, scheduleId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
        scheduleRepository.delete(schedule);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));
    }

    private void validateCreateRequest(ScheduleReqDto request) {
        if (isTrue(request.getIsPathIncluded()) && isTrue(request.getIsRepeat())) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_CANNOT_REPEAT_WITH_PATH);
        }
    }

    private boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }
}
