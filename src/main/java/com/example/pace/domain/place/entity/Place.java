package com.example.pace.domain.place.entity;

import com.example.pace.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    // FK
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String targetName;
    private Double targetLat;
    private Double targetLng;

    @Builder
    public Place(String targetName, Double targetLat, Double targetLng) {
        this.targetName = targetName;
        this.targetLat = targetLat;
        this.targetLng = targetLng;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}