package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.schedule.converter.PlaceReqDtoConverter;
import com.example.pace.domain.schedule.converter.ReminderReqDtoConverter;
import com.example.pace.domain.schedule.dto.request.ScheduleUpdateReqDto;
import com.example.pace.global.entity.BaseEntity;
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
import lombok.*;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule")
public class Schedule extends BaseEntity { // BaseEntity: created_at, updated_at

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Boolean isAllDay;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isRepeat;
    private String repeatGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_rule_id")
    private RepeatRule repeatRule;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "is_path_included")
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
    public void updateGeneralInfo(ScheduleUpdateReqDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getMemo() != null) this.memo = dto.getMemo();
        if (dto.getStartDate() != null) this.startDate = dto.getStartDate();
        if (dto.getEndDate() != null) this.endDate = dto.getEndDate();
        if (dto.getStartTime() != null) this.startTime = dto.getStartTime();
        if (dto.getEndTime() != null) this.endTime = dto.getEndTime();
        if (dto.getIsAllDay() != null) this.isAllDay = dto.getIsAllDay();
        if (dto.getIsPathIncluded() != null) this.isPathIncluded = dto.getIsPathIncluded();
    }

    public void updateDetailedInfo(ScheduleUpdateReqDto dto) {
        this.updateGeneralInfo(dto);

        if (dto.getPlace() != null) {
            Place newPlace = PlaceReqDtoConverter.toPlace(dto.getPlace());
            // 새 객체로 교체
            this.addPlace(newPlace);
        }

        // 알림 업데이트
        if (dto.getReminders() != null) {
            this.reminderList.clear();
            dto.getReminders().forEach(reminderDto ->
                    this.addReminder(ReminderReqDtoConverter.toReminder(reminderDto))
            );
        }
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