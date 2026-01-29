package com.example.pace.domain.schedule.logic;


import com.example.pace.domain.schedule.dto.request.ScheduleReqDto;
import com.example.pace.domain.schedule.enums.EndType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RepeatCalculator {

    public List<LocalDate> calculateDates(ScheduleReqDto.RepeatDto info, LocalDate start) {
        List<LocalDate> dates = new ArrayList<>();
        int interval = info.getRepeatInterval();

        switch (info.getRepeatType()) {
            case DAILY -> calculateDaily(dates, info, start, interval);
            case WEEKLY -> calculateWeekly(dates, info, start, interval);
            case YEARLY -> calculateYearly(dates, info, start, interval);
        }
        return dates;
    }

    // 일간 반복 계산
    private void calculateDaily(List<LocalDate> dates, ScheduleReqDto.RepeatDto info, LocalDate start, int interval) {
        LocalDate current = start;
        int count = 0;
        while (shouldContinue(info, current, count, start)) {
            dates.add(current);
            count++;
            current = current.plusDays(interval);
        }
    }

    // 연간 반복 계산
    private void calculateYearly(List<LocalDate> dates, ScheduleReqDto.RepeatDto info, LocalDate start, int interval) {
        LocalDate current = start;
        int count = 0;
        while (shouldContinue(info, current, count, start)) {
            dates.add(current);
            count++;
            current = current.plusYears(interval);
        }
    }

    // 요일 파싱 로직: "MONDAY,WEDNESDAY" -> 리스트 변환
    private List<DayOfWeek> parseDays(String daysOfWeek) {
        return Arrays.stream(daysOfWeek.split(","))
                .map(String::trim)
                .map(DayOfWeek::valueOf)
                .toList();
    }

    // 주간 반복 계산
    private void calculateWeekly(List<LocalDate> dates, ScheduleReqDto.RepeatDto info, LocalDate start, int interval) {
        List<DayOfWeek> targetDays = parseDays(info.getDaysOfWeek());

        if (info.getEndType() == EndType.COUNT) {
            for (int w = 0; w < info.getEndCount(); w++) {
                LocalDate weekBase = start.plusWeeks((long) w * interval);
                for (int d = 0; d < 7; d++) {
                    LocalDate current = weekBase.plusDays(d);
                    if (targetDays.contains(current.getDayOfWeek())) {
                        dates.add(current);
                    }
                }
            }
        } else {
            LocalDate current = start;
            while (shouldContinue(info, current, 0, start)) {
                if (targetDays.contains(current.getDayOfWeek())) {
                    dates.add(current);
                }
                current = current.plusDays(1);
                // 주 단위
                if (current.getDayOfWeek() == DayOfWeek.MONDAY && interval > 1) {
                    current = current.plusWeeks(interval - 1);
                }
            }
        }
    }

    // 공통 종료 조건 
    private boolean shouldContinue(ScheduleReqDto.RepeatDto info, LocalDate current, int count, LocalDate start) {
        return switch (info.getEndType()) {
            case COUNT -> count < info.getEndCount();
            case DATE -> !current.isAfter(info.getRepeatEndDate());
            case FOREVER -> !current.isAfter(start.plusYears(2));
        };
    }
}