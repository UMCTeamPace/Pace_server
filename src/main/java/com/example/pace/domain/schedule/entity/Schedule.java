package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "schedule",
        indexes = {
                // 커서 페이징 최적화 인덱스 칼럼 지정
                @Index(name = "idx_schedule_member_date_id", columnList = "member_id, start_date, id"),
        }
)
public class Schedule extends BaseEntity { // BaseEntity: created_at, updated_at

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    private String calendarId;
    private String color;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "is_path_included", nullable = false)
    private Boolean isPathIncluded; // 경로 포함 여부

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Route route;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Place place;

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminderList = new ArrayList<>();

    public void addReminder(Reminder reminder) {
        this.reminderList.add(reminder);
        reminder.setSchedule(this);
    }

    public void addPlace(Place place) {
        this.place = place;
        place.setSchedule(this);
    }

    //schedule에 route 붙이기
    public void addRoute(Route route) {
        this.route = route;
        route.setSchedule(this);
    }

    public void removeRoute() {
        if (this.route != null) {
            this.route.setSchedule(null);
            this.route = null;
        }
    }
}