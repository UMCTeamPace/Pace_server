package com.example.pace.domain.member.repository;

public interface SavedPlaceRepositoryCustom {
    // 해당 유저의 같은 그룹에 같은 장소가 이미 있는지 확인하는 메서드
    boolean isPlaceSavedInGroup(Long memberId, String placeId, String groupName);
}
