package com.example.pace.domain.member.dto.response;

import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.entity.Setting;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.stream.Collectors;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "사용자 설정 조회 응답 DTO")
public class SettingResponse {

    private Long settingId;

    private int earlyArrivalTime;
    private boolean isNotiEnabled;
    private boolean isLocEnabled;

    private boolean isReminderActive;

    private CalendarType calendarType;

    private List<Integer> scheduleReminderTimes;
    private List<Integer> departureReminderTimes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SettingResponse from(Setting setting) {
        return SettingResponse.builder()
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isReminderActive(setting.isReminderActive())
                .calendarType(setting.getCalendarType())
                .scheduleReminderTimes(setting.getScheduleReminderTimes())
                .departureReminderTimes(setting.getDepartureReminderTimes())
                .build();
    }

    // PATCH 응답용 최소 DTO
    @Getter
    @Builder
    public static class Simple {
        private Long settingId;
        private LocalDateTime updatedAt;

        public static Simple from(SettingResponse response) {
            return Simple.builder()
                    .settingId(response.getSettingId())
                    .updatedAt(response.getUpdatedAt())
                    .build();
        }
    }
}
