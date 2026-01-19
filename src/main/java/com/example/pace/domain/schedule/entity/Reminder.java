package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.schedule.enums.ReminderType;
import com.example.pace.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

}