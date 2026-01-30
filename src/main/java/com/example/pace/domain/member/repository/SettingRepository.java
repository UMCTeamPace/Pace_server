package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByMember(Member member);
}