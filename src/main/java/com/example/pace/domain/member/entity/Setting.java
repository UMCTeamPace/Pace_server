package com.example.pace.domain.member.entity;

import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.global.entity.BaseEntity;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.converter.SettingConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 알림 권한 허용 여부
    @Column(name = "is_noti_enabled", nullable = false)
    private Boolean isNotiEnabled;

    // 위치 권한 허용 여부
    @Column(name = "is_loc_enabled", nullable = false)
    private Boolean isLocEnabled;

    // 미리 도착(분)
    @Column(name = "early_arrival_time", nullable = false)
    private Integer earlyArrivalTime;

    // (추가) UI/온보딩에서 설정
    // 리마인더(일정 알림) 사용 여부
    @Column(name = "is_reminder_active", nullable = false)
    private Boolean isReminderActive;

    // 캘린더 선택
    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_type", nullable = false, length = 30)
    private CalendarType calendarType;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReminderTime> reminderTimes = new ArrayList<>();


    public static com.example.pace.domain.member.entity.Setting defaultOf(Member member) {
        return com.example.pace.domain.member.entity.Setting.builder()
                .member(member)
                .isNotiEnabled(false)      // 권한 허용(또는 허용 유무)
                .isLocEnabled(false)
                .earlyArrivalTime(20)
                .isReminderActive(true)
                .calendarType(CalendarType.GOOGLE) // 프로젝트 기본값에 따라 달라짐
                .build();
    }

    public void replaceReminderTimes(AlarmType alarmType, List<Integer> minutesList) {
        this.reminderTimes.removeIf(rt -> rt.getAlarmType() == alarmType);

        if (minutesList.isEmpty()) { return; }

        minutesList.stream()
                .filter(Objects::nonNull)
                .filter(m -> m > 0)
                .distinct()
                .forEach(minutes ->
                        this.reminderTimes.add(
                                SettingConverter.toEntity(this, alarmType, minutes)
                        )
                );
    }

    public List<Integer> getScheduleReminderTimes() {
        return reminderTimes.stream()
                .filter(rt -> rt.getAlarmType() == AlarmType.SCHEDULE)
                .map(ReminderTime::getMinutes)
                .toList();
    }
    // 명시적 getter Converter때문에 사용(Lombok 우회)
    public List<Integer> getDepartureReminderTimes() {
        return reminderTimes.stream()
                .filter(rt -> rt.getAlarmType() == AlarmType.DEPARTURE)
                .map(ReminderTime::getMinutes)
                .toList();
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

    // 명시적 getter Converter때문에 사용(Lombok 우회)

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
