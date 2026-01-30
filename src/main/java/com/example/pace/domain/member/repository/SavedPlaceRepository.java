package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.SavedPlace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long>, SavedPlaceRepositoryCustom {
    List<SavedPlace> findAllByIdInAndMemberId(List<Long> placeIdList, Long memberId);
}
