package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}