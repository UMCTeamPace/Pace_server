package com.example.pace.domain.member.entity;

import com.example.pace.domain.member.enums.AlarmType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(
        name = "reminder_time",
        indexes = {
                // 세팅의 반복 시간을 조회하기 위한 인덱스 칼럼 지정
                @Index(name = "idx_reminder_time_setting", columnList = "setting_id")
        }
)
public class ReminderTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id", nullable = false)
    private Setting setting;

    @Column(nullable = false)
    private Integer minutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false, length = 20)
    private AlarmType alarmType;
}
