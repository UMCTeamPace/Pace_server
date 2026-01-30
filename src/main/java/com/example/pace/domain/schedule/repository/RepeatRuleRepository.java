package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.RepeatRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepeatRuleRepository extends JpaRepository<RepeatRule, Long> {
}