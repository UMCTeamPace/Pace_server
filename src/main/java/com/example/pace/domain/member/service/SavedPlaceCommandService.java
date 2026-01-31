package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SavedPlaceConverter;
import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.PlaceGroup;
import com.example.pace.domain.member.entity.SavedPlace;
import com.example.pace.domain.member.exception.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.exception.PlaceGroupErrorCode;
import com.example.pace.domain.member.exception.PlaceGroupException;
import com.example.pace.domain.member.exception.SavedPlaceErrorCode;
import com.example.pace.domain.member.exception.SavedPlaceException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.PlaceGroupRepository;
import com.example.pace.domain.member.repository.SavedPlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SavedPlaceCommandService {
    private final SavedPlaceRepository savedPlaceRepository;
    private final MemberRepository memberRepository;
    private final PlaceGroupRepository placeGroupRepository;

    // 장소 저장
    public SavedPlaceResDTO.PlaceDTO savePlace(Long memberId, SavedPlaceReqDTO.SavedPlaceDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        PlaceGroup placeGroup = placeGroupRepository.findByMemberIdAndId(memberId, request.getGroupId())
                .orElseThrow(() -> new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_NOT_FOUND));

        if (savedPlaceRepository.isPlaceSavedInGroup(memberId, request.getPlaceId(), request.getGroupId())) {
            throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_ALREADY_EXISTS);
        }

        SavedPlace savedPlace = SavedPlaceConverter.toEntity(request, member);
        placeGroup.addSavedPlace(savedPlace);

        return SavedPlaceConverter.toPlaceDTO(savedPlaceRepository.save(savedPlace));
    }

    // 장소들 삭제
    public void deletePlaces(Long memberId, List<Long> placeIdList) {
        List<SavedPlace> myPlaceList = savedPlaceRepository.findAllByIdInAndMemberId(placeIdList, memberId);

        // 요청한 id 개수와 실제 조회된 찾은 개수가 다르면 에러
        if (myPlaceList.size() != placeIdList.size()) {
            throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_NOT_FOUND);
        }

        savedPlaceRepository.deleteAllInBatch(myPlaceList);
    }

    // 장소들 이동
    public void movePlaces(Long memberId, SavedPlaceReqDTO.MovePlaceListDTO request) {
        List<Long> placeIdList = request.getPlaceIdList();
        Long targetGroupId = request.getTargetGroupId();

        PlaceGroup targetGroup = placeGroupRepository.findByMemberIdAndId(memberId, targetGroupId)
                .orElseThrow(() -> new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_NOT_FOUND));

        // 이동할 장소들 조회
        List<SavedPlace> placesToMove = savedPlaceRepository.findAllByIdInAndMemberId(placeIdList, memberId);

        if (placesToMove.size() != placeIdList.size()) {
            throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_UNAUTHORIZED);
        }

        List<String> targetPlaceIdList = targetGroup.getSavedPlaceList().stream()
                .map(SavedPlace::getPlaceId).toList();

        for (SavedPlace place : placesToMove) {
            // 이동할 그룹에 이미 이 장소가 있는지 체크
            if (targetPlaceIdList.contains(place.getPlaceId())) {
                throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_ALREADY_EXISTS);
            }

            targetGroup.addSavedPlace(place);
        }
    }
}
