package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.entity.QPlaceGroup;
import com.example.pace.domain.member.entity.QSavedPlace;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceGroupRepositoryImpl implements PlaceGroupRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final static QPlaceGroup placeGroup = QPlaceGroup.placeGroup;
    private final static QSavedPlace savedPlace = QSavedPlace.savedPlace;

    @Override
    public List<PlaceGroupResDTO.PlaceGroupQueryDTO> findAllGroupInfoByMemberId(Long memberId) {
        return queryFactory
                .select(Projections.fields(
                        PlaceGroupResDTO.PlaceGroupQueryDTO.class,
                        placeGroup.id.as("groupId"),
                        placeGroup.groupName,
                        placeGroup.groupColor,
                        placeGroup.createdAt,
                        savedPlace.count().as("placeCount")
                ))
                .from(placeGroup)
                .leftJoin(placeGroup.savedPlaceList, savedPlace)
                .where(placeGroup.member.id.eq(memberId))
                .groupBy(placeGroup.id)
                .orderBy(placeGroup.createdAt.desc())
                .fetch();
    }
}