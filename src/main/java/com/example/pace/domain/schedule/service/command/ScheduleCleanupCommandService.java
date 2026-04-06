package com.example.pace.domain.schedule.service.command;

import com.example.pace.domain.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleCleanupCommandService {

    private final ScheduleRepository scheduleRepository;

    // 매일 새벽 4시에 삭제
    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void cleanupOldSchedules() {
        LocalDate cutoffDate = LocalDate.now().minusDays(30);
        scheduleRepository.deleteAllByEndDateBefore(cutoffDate);
        log.info("(test) 일정 자동 삭제 완료");
    }
}
