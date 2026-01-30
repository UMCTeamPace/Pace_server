package com.example.pace.domain.member.repository;

import static com.example.pace.domain.member.entity.QSavedPlace.savedPlace;

import com.example.pace.domain.member.entity.SavedPlace;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SavedPlaceRepositoryImpl implements SavedPlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isPlaceSavedInGroup(Long memberId, String placeId, Long groupId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(savedPlace)
                .where(
                        savedPlace.member.id.eq(memberId),
                        savedPlace.placeId.eq(placeId),
                        savedPlace.placeGroup.id.eq(groupId)
                )
                .fetchFirst(); // limit 1과 동일(결과가 없으면 null 반환)

        return fetchOne != null;
    }

    @Override
    public List<SavedPlace> findAllPlaceByMemberAndGroupId(Long memberId, Long groupId) {
        return queryFactory
                .selectFrom(savedPlace)
                .where(
                        savedPlace.member.id.eq(memberId),
                        savedPlace.placeGroup.id.eq(groupId)
                )
                .orderBy(savedPlace.createdAt.desc()) // 최신순 정렬
                .fetch();
    }
}
