package com.example.pace.domain.member.dto.response;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.entity.Setting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "사용자 설정 조회 응답 DTO")
public class SettingResponseDTO {
    private Long settingId;

    private Integer earlyArrivalTime;
    private Boolean isNotiEnabled;
    private Boolean isLocEnabled;

    private Boolean isReminderActive;

    private CalendarType calendarType;

    private List<Integer> scheduleReminderTimes;
    private List<Integer> departureReminderTimes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SettingResponseDTO from(Setting setting) {
        return SettingConverter.toResponse(setting);
    }
}
