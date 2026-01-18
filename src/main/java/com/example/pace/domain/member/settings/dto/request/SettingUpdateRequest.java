package com.example.pace.domain.member.settings.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingUpdateRequest {
    private Integer earlyArrivalTime;
    private Boolean isNotiEnabled;
    private Boolean isLocEnabled;
}