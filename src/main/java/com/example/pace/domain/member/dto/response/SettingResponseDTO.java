package com.example.pace.domain.member.dto.response;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.enums.AlarmType;
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
    private List<AlarmConfig> alarms;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public record AlarmConfig(
            AlarmType type,
            List<Integer> minutes
    ) {}

    public static SettingResponseDTO from(Setting setting) {
        return SettingConverter.toResponse(setting);
    }

}
