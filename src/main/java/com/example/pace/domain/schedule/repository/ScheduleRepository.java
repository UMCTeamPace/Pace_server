package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * 멤버의 일정 목록을 커서 기반 무한 스크롤로 조회
     * 마지막 조회 날짜보다 크거나 날짜가 같으면 ID가 큰 일정부터 조회
     */
    @Query("select distinct s from Schedule s " +
            "left join fetch s.place " +
            "left join fetch s.reminderList " +
            "where s.member.id = :memberId " +
            "and (s.startDate > :lastDate or (s.startDate = :lastDate and s.id > :lastId)) " +
            "and s.startDate <= :endDate " +
            "order by s.startDate asc, s.id asc")
    List<Schedule> findAllByMemberAndDateRange(
            @Param("memberId") Long memberId,
            @Param("lastDate") LocalDate lastDate,
            @Param("lastId") Long lastId,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    Long member(Member member);
}
