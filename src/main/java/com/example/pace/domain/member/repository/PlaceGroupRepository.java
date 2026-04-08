package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.PlaceGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceGroupRepository extends JpaRepository<PlaceGroup, Long>, PlaceGroupRepositoryCustom {
    // 단일 그룹만을 조회
    Optional<PlaceGroup> findByMemberIdAndId(Long memberId, Long groupId);

    // 그룹명 중복을 방지하기 위해 기존에 같은 그룹명이 있는지 확인
    boolean existsByMemberIdAndGroupName(Long memberId, String groupName);

    // 조회 시 해당 회원의 모든 그룹 정보를 가져오되 내림차순 정렬 후 조회
    List<PlaceGroup> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);

    // 해당 회원의 그룹들만 필터링해서 조회
    List<PlaceGroup> findAllByIdInAndMemberId(List<Long> groupId, Long memberId);
}
