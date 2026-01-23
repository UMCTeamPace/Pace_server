package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.enums.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReminderTimeRepository extends JpaRepository<ReminderTime, Long> {

}
