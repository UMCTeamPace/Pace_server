package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.PlaceGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceGroupRepository extends JpaRepository<PlaceGroup, Long> {
    Optional<PlaceGroup> findByMemberIdAndId(Long memberId, Long groupId);

    boolean existsByMemberIdAndGroupName(Long memberId, String groupName);

    List<PlaceGroup> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
