package com.example.pace.domain.member.settings.entity;

import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reminder_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReminderTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Long reminderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @Column(name = "time_minutes", nullable = false)
    private int timeMinutes;

    public static ReminderTime of(Setting setting, int timeMinutes) {
        return ReminderTime.builder()
                .setting(setting)
                .timeMinutes(timeMinutes)
                .build();
    }
}
