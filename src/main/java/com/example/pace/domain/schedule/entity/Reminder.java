package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.schedule.enums.ReminderType;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reminder",
        indexes = {
                // 일정으로 알림 관련 정보를 조회하기 위한 인덱스 칼럼 지정
                @Index(name = "idx_reminder_schedule", columnList = "schedule_id")
        }
)
public class Reminder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_type", nullable = false)
    private ReminderType reminderType;

    @Column(name = "minutes_before", nullable = false)
    private Integer minutesBefore;

    @Column(name = "reminder_enabled")
    private Boolean reminderEnabled; // 알림 켜짐/꺼짐 여부
}