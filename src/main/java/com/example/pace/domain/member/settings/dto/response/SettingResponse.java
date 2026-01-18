package com.example.pace.domain.member.settings.dto.response;

import com.example.pace.domain.member.settings.entity.Setting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettingResponse {
    private Long settingId;
    private Integer earlyArrivalTime;
    private Boolean isNotiEnabled;
    private Boolean isLocEnabled;

    public static SettingResponse from(Setting setting) {
        return SettingResponse.builder()
                .settingId(setting.getId())
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isNotiEnabled(setting.isNotiEnabled())
                .isLocEnabled(setting.isLocEnabled())
                .build();
    }
}