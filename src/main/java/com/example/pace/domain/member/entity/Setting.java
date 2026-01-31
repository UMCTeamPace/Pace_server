package com.example.pace.domain.member.entity;

import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(
        name = "setting",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_setting_member",
                columnNames = "member_id"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Setting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "is_noti_enabled", nullable = false)
    private Boolean isNotiEnabled;

    @Column(name = "is_loc_enabled", nullable = false)
    private Boolean isLocEnabled;

    @Column(name = "early_arrival_time", nullable = false)
    private Integer earlyArrivalTime;

    @Column(name = "is_reminder_active", nullable = false)
    private Boolean isReminderActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_type", nullable = false, length = 30)
    private CalendarType calendarType;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReminderTime> reminderTimes = new ArrayList<>();

    //알림 교체
    public void replaceReminderTimes(AlarmType alarmType, List<ReminderTime> newTimes) {
        this.reminderTimes.removeIf(rt -> rt.getAlarmType() == alarmType);
        this.reminderTimes.addAll(newTimes);
    }

    public void update(
            Integer earlyArrivalTime,
            Boolean isNotiEnabled,
            Boolean isLocEnabled,
            Boolean isReminderActive,
            CalendarType calendarType
    ) {
        if (earlyArrivalTime != null) this.earlyArrivalTime = earlyArrivalTime;
        if (isNotiEnabled != null) this.isNotiEnabled = isNotiEnabled;
        if (isLocEnabled != null) this.isLocEnabled = isLocEnabled;
        if (isReminderActive != null) this.isReminderActive = isReminderActive;
        if (calendarType != null) this.calendarType = calendarType;
    }

    // Lombok Boolean Getter 우회
    public boolean isNotiEnabled() {
        return this.isNotiEnabled;
    }

    public boolean isLocEnabled() {
        return this.isLocEnabled;
    }

    public boolean isReminderActive() {
        return this.isReminderActive;
    }
}
