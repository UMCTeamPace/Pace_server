package com.example.pace.domain.member.settings.dto.response;

import com.example.pace.domain.member.settings.entity.CalendarType;
import com.example.pace.domain.member.settings.entity.Setting;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingResponse {

    private Long settingId;

    private int earlyArrivalTime;
    private boolean isNotiEnabled;
    private boolean isLocEnabled;

    private boolean isReminderActive;
    private int deptReminderFreq;
    private int deptReminderInterval;

    private CalendarType calendarType;

    // reminder_time 테이블 -> List<Integer>
    private List<Integer> reminderTimes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SettingResponse from(Setting setting) {
        return SettingResponse.builder()
                .settingId(setting.getSettingId())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .isReminderActive(setting.isReminderActive())
                .deptReminderFreq(setting.getDeptReminderFreq())
                .deptReminderInterval(setting.getDeptReminderInterval())
                .calendarType(setting.getCalendarType())

                // reminder_time -> minutes만 뽑아서 List<Integer>
                .reminderTimes(
                        setting.getReminderTimes().stream()
                                .map(rt -> rt.getTimeMinutes())
                                .toList()
                )

                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
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
