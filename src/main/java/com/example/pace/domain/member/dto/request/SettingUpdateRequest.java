package com.example.pace.domain.member.dto.request;

import com.example.pace.domain.member.enums.CalendarType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingUpdateRequest {
    // 미리도착추천시간(분)
    private Integer earlyArrivalTime;

    // 알림 권한 허용 여부
    private Boolean isNotiEnabled;

    // 위치 권한 허용 여부
    private Boolean isLocEnabled;

    // 리마인드 알림 사용 여부
    private Boolean isReminderActive;

    // (출발 알림) 몇 번 알릴지
    private Integer deptReminderFreq;

    // (출발 알림) 몇 분 전에 알림 울릴지(간격)
    private Integer deptReminderInterval;

    // 캘린더 선택
    private CalendarType calendarType;

    // reminder_time 다중 선택
    private List<Integer> reminderTimes;

    // 일정 알림
    private List<Integer> scheduleReminderTimes;

    //출발 알림
    private List<Integer> departureReminderTimes;
}