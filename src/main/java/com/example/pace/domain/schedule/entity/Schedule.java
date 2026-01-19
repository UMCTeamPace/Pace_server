package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.reminder.entity.Reminder;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.place.entity.Place;
import com.example.pace.global.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class Schedule extends BaseEntity { // BaseEntity: created_at, updated_at

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "is_path_included")
    private Boolean isPathIncluded; // 경로 포함 여부


    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Route route;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Place place;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders = new ArrayList<>();

    @Builder
    public Schedule(Member member, String title, LocalDate startDate, LocalDate endDate,
                    LocalTime startTime, LocalTime endTime, String memo,
                    Boolean isPathIncluded) {
        this.member = member;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
        this.isPathIncluded = isPathIncluded;
    }

    // 연관관계 편의 메서드
    public void setPlace(Place place) {
        this.place = place;
        place.setSchedule(this);
    }

    public void setRoute(Route route) {
        this.route = route;
        route.setSchedule(this);
    }

    public void addReminder(Reminder reminder) {
        this.reminders.add(reminder);
        reminder.setSchedule(this);
    }
}