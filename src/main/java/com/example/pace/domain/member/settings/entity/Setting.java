package com.example.pace.domain.member.settings.entity;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "setting_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 알림 권한 허용 여부
    @Column(name = "is_noti_enabled", nullable = false)
    private boolean isNotiEnabled;

    // 위치 권한 허용 여부
    @Column(name = "is_loc_enabled", nullable = false)
    private boolean isLocEnabled;

    // 미리 도착(분)
    @Column(name = "early_arrival_time", nullable = false)
    private int earlyArrivalTime;

    // (추가) UI/온보딩에서 설정
    // 리마인더(일정 알림) 사용 여부
    @Column(name = "is_reminder_active", nullable = false)
    private boolean isReminderActive;

    // 출발 알림 몇 번 알릴지 (예: 1,2,3)
    @Column(name = "dept_reminder_freq", nullable = false)
    private int deptReminderFreq;

    // 출발 알림 몇 분 간격으로 (예: 5,10)
    @Column(name = "dept_reminder_interval", nullable = false)
    private int deptReminderInterval;

    // 캘린더 선택
    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_type", nullable = false, length = 30)
    private CalendarType calendarType;


    // reminder_time (1:N)
    @OneToMany(mappedBy = "setting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReminderTime> reminderTimes = new ArrayList<>();

    public static Setting defaultOf(Member member) {
        return Setting.builder()
                .member(member)
                .isNotiEnabled(true)      // 권한 허용(또는 허용 유무)
                .isLocEnabled(false)
                .earlyArrivalTime(20)
                .isReminderActive(true)
                .deptReminderFreq(1)
                .deptReminderInterval(5)
                .calendarType(CalendarType.GOOGLE) // 프로젝트 기본값에 따라 달라짐
                .build();
    }

    //DTO에서 stream으로 minutes만 뽑아오기
    public List<ReminderTime> getReminderTimes() {
        return reminderTimes;
    }

    public void replaceReminderTimes(List<Integer> minutesList) {
        this.reminderTimes.clear();

        if (minutesList == null) return;

        for (Integer minutes : minutesList) {
            if (minutes == null) continue;
            this.reminderTimes.add(ReminderTime.of(this, minutes));
        }
    }

    public void addReminderTime(int minutes) {
        this.reminderTimes.add(ReminderTime.of(this, minutes));
    }

    public void update(
            Integer earlyArrivalTime,
            Boolean isNotiEnabled,
            Boolean isLocEnabled,
            Boolean isReminderActive,
            Integer deptReminderFreq,
            Integer deptReminderInterval,
            CalendarType calendarType
    ) {
        if (earlyArrivalTime != null) this.earlyArrivalTime = earlyArrivalTime;
        if (isNotiEnabled != null) this.isNotiEnabled = isNotiEnabled;
        if (isLocEnabled != null) this.isLocEnabled = isLocEnabled;
        if (isReminderActive != null) this.isReminderActive = isReminderActive;
        if (deptReminderFreq != null) this.deptReminderFreq = deptReminderFreq;
        if (deptReminderInterval != null) this.deptReminderInterval = deptReminderInterval;
        if (calendarType != null) this.calendarType = calendarType;
    }
}
