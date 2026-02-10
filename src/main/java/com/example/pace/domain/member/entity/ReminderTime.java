package com.example.pace.domain.member.entity;

import com.example.pace.domain.member.enums.AlarmType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReminderTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @Column(nullable = false)
    private Integer minutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false, length = 20)
    private AlarmType alarmType;

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
