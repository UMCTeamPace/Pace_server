package com.example.pace.domain.member.dto.request;

import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OnboardingReqDTO (
        @NotNull Boolean isReminderActive,

        @Min(0) @Max(240) Integer earlyArrivalTime,   // 정책에 맞게 상한 조절

        @NotNull CalendarType calendarType,                           // enum이면 enum으로 바꾸기

        @Valid
        List<AlarmConfig> alarms
){
    public record AlarmConfig(
            @NotNull AlarmType type,
            List<@Min(1) @Max(1440) Integer> minutes    // 1~1440분 예시
    ) {}
}
