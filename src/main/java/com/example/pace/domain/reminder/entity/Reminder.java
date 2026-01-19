package com.example.pace.domain.reminder.entity;

import com.example.pace.domain.reminder.enums.ReminderType;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reminder")
public class Reminder extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Long id;

    // FK
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

    @Builder
    public Reminder(ReminderType reminderType, Integer minutesBefore, Boolean reminderEnabled) {
        this.reminderType = reminderType;
        this.minutesBefore = minutesBefore;
        // 생성될 때는 기본적으로 True
        this.reminderEnabled = reminderEnabled != null ? reminderEnabled : true;
    }

    // 연관관계 편의 메서드
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}