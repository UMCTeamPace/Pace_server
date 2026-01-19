package com.example.pace.domain.member.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "setting_alarm_offset",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_setting_alarm_offset",
                columnNames = {"setting_id", "alarm_type", "offset_minutes"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SettingAlarmOffset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offset_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false)
    private AlarmType alarmType;

    // 예: 5, 10, 15, 30, 60, 120, 1440(1일), 2880(2일), 10080(1주)
    @Column(name = "offset_minutes", nullable = false)
    private int offsetMinutes;

    public static SettingAlarmOffset of(Setting setting, AlarmType type, int minutes) {
        return SettingAlarmOffset.builder()
                .setting(setting)
                .alarmType(type)
                .offsetMinutes(minutes)
                .build();
    }
}