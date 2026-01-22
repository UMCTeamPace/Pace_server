package com.example.pace.domain.schedule.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleReqDtoConverter;
import com.example.pace.domain.schedule.converter.ScheduleResDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto.ReminderDto;
import com.example.pace.domain.schedule.dto.response.ScheduleResDto;
import com.example.pace.domain.schedule.entity.Reminder;
import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
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


    @Transactional
    public ScheduleResDto createSchedule(Long memberId, ScheduleReqDto request) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        // 일정 저장
        Schedule schedule = ScheduleReqDtoConverter.toSchedule(request);

        member.addSchedule(schedule);


        // 장소 저장
        // 조건 - 경로 포함X(False)+장소 정보가 있을 때
        if (!Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getPlace() != null) {
            schedule.addPlace(PlaceReqDtoConverter.toPlace(request.getPlace()));
        }

        // 알림 저장
        if (request.getReminders() != null && !request.getReminders().isEmpty()) {
            List<Reminder> reminderList = new ArrayList<>();
            for(ReminderDto reminderDto: request.getReminders()) {
                reminderList.add(ReminderReqDtoConverter.toReminder(reminderDto));
            }

            reminderList.forEach(schedule::addReminder);
        }

        /* * 경로 저장
         * if (Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getRoute() != null) {
         * }
         */


        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResDtoConverter.toScheduleResDto(savedSchedule);
    }

    // 일정 상세 조회
    public ScheduleResDto getSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow();

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
}