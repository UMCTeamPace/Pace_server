package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.member.entity.Member;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private String targetName;
    private BigDecimal targetLat;
    private BigDecimal targetLng;

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;

    }
}