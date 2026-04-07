package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * 멤버의 일정 목록을 커서 기반 무한 스크롤로 조회
     * 마지막 조회 날짜보다 크거나 날짜가 같으면 ID가 큰 일정부터 조회
     */

    //AND조건으로 조회하기
    Optional<Schedule> findByIdAndMemberId(Long id, Long memberId);


    @Query("select s from Schedule s " +
            "left join fetch s.place " +
            "where s.member.id = :memberId " +
            "and (s.startDate > :lastDate or (s.startDate = :lastDate and s.id > :lastId)) " +
            "and s.startDate <= :endDate " +
            "order by s.startDate asc, s.id asc")
    Slice<Schedule> findAllByMemberAndDateRange(
            @Param("memberId") Long memberId,
            @Param("lastDate") LocalDate lastDate,
            @Param("lastId") Long lastId,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
    Optional<Schedule> findByMemberIdAndId(Long memberId, Long scheduleId);

    @Query("select s from Schedule s join fetch s.member where s.id in :ids")
    List<Schedule> findAllWithMemberByIdIn(@Param("ids") List<Long> ids, @Param("memberId") Long memberId);

    // 자식들 먼저 벌크 삭제
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Reminder r WHERE r.schedule.endDate < :date")
    void deleteRemindersByScheduleEndDateBefore(@Param("date") LocalDate date);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Route r WHERE r.schedule.endDate < :date")
    void deleteRoutesByScheduleEndDateBefore(@Param("date") LocalDate date);
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Place p WHERE p.schedule.endDate < :date")
    void deletePlacesByScheduleEndDateBefore(@Param("date") LocalDate date);
    // 부모 (Schedule) 벌크 삭제
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Schedule s WHERE s.endDate < :date")
    void deleteSchedulesByEndDateBefore(@Param("date") LocalDate date);
}
