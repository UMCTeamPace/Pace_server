package com.example.pace.domain.member.dto.request;

import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingUpdateRequestDTO {
    // 미리도착추천시간(분)
    private Integer earlyArrivalTime;

    // 알림 권한 허용 여부
    private Boolean isNotiEnabled;

    // 위치 권한 허용 여부
    private Boolean isLocEnabled;

    // 리마인드 알림 사용 여부
    private Boolean isReminderActive;

    // 캘린더 선택
    private CalendarType calendarType;

    //alarms로 받기 위해
    private List<Alarm> alarms;

    @Getter
    @NoArgsConstructor
    public static class Alarm {
        private AlarmType type;        // "SCHEDULE", "DEPARTURE"
        @Size(max = 5, message = "알람 시간은 타입별로 최대 5개까지 설정할 수 있습니다.")
        private List<Integer> minutes;
    }
}