package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.SavedPlace;
import java.util.List;

public interface SavedPlaceRepositoryCustom {
    // 해당 유저의 같은 그룹에 같은 장소가 이미 있는지 확인하는 메서드
    boolean isPlaceSavedInGroup(Long memberId, String placeId, String groupName);

    // 해당 유저의 그룹 내에 저장된 장소 목록 조회
    List<SavedPlace> findAllPlaceByMemberAndGroupName(Long memberId, String groupName);
}
