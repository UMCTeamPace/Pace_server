package com.example.pace.domain.schedule.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.place.entity.Place;
import com.example.pace.domain.reminder.entity.Reminder;
import com.example.pace.domain.reminder.enums.ReminderType;
import com.example.pace.domain.schedule.dto.request.ScheduleRequestDto;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 성능 최적화 (쓰기 작업은 메서드에 따로)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createSchedule(Long memberId, ScheduleRequestDto request) {
        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        // 일정 생성
        Schedule schedule = Schedule.builder()
                .member(member)
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .memo(request.getMemo())
                .isPathIncluded(request.getIsPathIncluded())
                .build();

        // 장소 저장
        // 조건 - 경로 포함X(False)+장소 정보가 있을 때
        if (!Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getPlace() != null) {
            ScheduleRequestDto.PlaceDto placeDto = request.getPlace();

            Place place = Place.builder()
                    .targetName(placeDto.getTargetName())
                    .targetLat(placeDto.getTargetLat())
                    .targetLng(placeDto.getTargetLng())
                    .build();

            schedule.setPlace(place); // 연관관계 설정
        }

        // 알림 저장
        if (request.getReminders() != null && !request.getReminders().isEmpty()) {
            for (ScheduleRequestDto.ReminderDto reminderDto : request.getReminders()) {
                Reminder reminder = Reminder.builder()
                        .reminderType(ReminderType.valueOf(reminderDto.getReminderType())) // String -> Enum 변환
                        .minutesBefore(reminderDto.getMinutesBefore())
                        .reminderEnabled(true) // 기본값 켜짐
                        .build();

                schedule.addReminder(reminder); // 연관관계 설정
            }
        }

        /* * 경로 저장
         * if (Boolean.TRUE.equals(request.getIsPathIncluded()) && request.getRoute() != null) {
         * }
         */

        // 최종 저장 (일정 + 장소 + 알림)
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return savedSchedule.getId(); // 생성된 일정 ID 반환
    }
}