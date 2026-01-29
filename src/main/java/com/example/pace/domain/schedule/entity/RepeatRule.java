package com.example.pace.domain.schedule.entity;

import com.example.pace.domain.schedule.enums.EndType;
import com.example.pace.domain.schedule.enums.RepeatType;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepeatRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 반복 주기: DAILY, WEEKLY, MONTHLY, YEARLY
    @Enumerated(EnumType.STRING)
    private RepeatType repeatType;

    // 반복 간격: N일, N주
    private Integer repeatInterval;

    // 요일 반복 설정
    private String daysOfWeek;

    // 종료 조건 종류: FOREVER(계속), COUNT(횟수), DATE(날짜)
    @Enumerated(EnumType.STRING)
    private EndType endType;

    private Integer endCount; // 반복 횟수
    private LocalDate endDate; // 종료 날짜
}