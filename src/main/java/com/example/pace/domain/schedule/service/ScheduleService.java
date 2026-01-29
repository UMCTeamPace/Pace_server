package com.example.pace.domain.schedule.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.schedule.converter.ScheduleReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleResDtoConverter;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.RepeatRule;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.exception.ScheduleErrorCode;
import com.example.pace.domain.schedule.logic.RepeatCalculator;
import com.example.pace.domain.schedule.logic.ScheduleFactory;
import com.example.pace.domain.schedule.repository.RepeatRuleRepository;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import com.example.pace.global.apiPayload.code.GeneralErrorCode;
import com.example.pace.global.apiPayload.exception.GeneralException;
import java.util.UUID;
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
@Transactional(readOnly = true) // 조회 성능 최적화 (쓰기 작업은 메서드에 따로)
public class  ScheduleService {

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

        List<LocalDate> dates;
        if (Boolean.TRUE.equals(request.getIsRepeat()) && request.getRepeatInfo() != null) {
            dates = repeatCalculator.calculateDates(request.getRepeatInfo(), request.getStartDate());
        } else {
            dates = List.of(request.getStartDate());
        }

        List<Schedule> scheduleList = dates.stream()
                .map(date -> scheduleFactory.create(member, date, request, repeatRule, groupId))
                .toList();

        List<Schedule> savedSchedules = scheduleRepository.saveAll(scheduleList);

        if (savedSchedules.isEmpty()) {
            throw new GeneralException(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
        }

        return ScheduleResDtoConverter.toScheduleResDto(savedSchedules.get(0));
    }


    // 일정 상세 조회
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
        Slice<Schedule> schedules = scheduleRepository.findAllByMemberAndDateRange(memberId,cursorDate, cursorId, maxSearchDate, pageable);

        return schedules.map(ScheduleResDtoConverter::toScheduleResDto);
    }

    @Transactional
    public void deleteSchedule(Long memberId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByMemberIdAndId(memberId, scheduleId)
                .orElseThrow(()-> new GeneralException(GeneralErrorCode.NOT_FOUND));
        scheduleRepository.deleteById(scheduleId);
    }
}