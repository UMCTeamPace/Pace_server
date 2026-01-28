package com.example.pace.domain.member.repository;

import static com.example.pace.domain.member.entity.QSavedPlace.savedPlace;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SavedPlaceRepositoryImpl implements SavedPlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isPlaceSavedInGroup(Long memberId, String placeId, String groupName) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(savedPlace)
                .where(
                        savedPlace.member.id.eq(memberId),
                        savedPlace.placeId.eq(placeId),
                        savedPlace.groupName.eq(groupName)
                )
                .fetchFirst(); // limit 1과 동일(결과가 없으면 null 반환)

        return fetchOne != null;
    }
}
