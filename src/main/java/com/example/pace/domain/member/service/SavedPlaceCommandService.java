package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SavedPlaceConverter;
import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.SavedPlace;
import com.example.pace.domain.member.exception.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SavedPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SavedPlaceCommandService {
    private final SavedPlaceRepository savedPlaceRepository;
    private final MemberRepository memberRepository;

    // 장소 저장
    public SavedPlaceResDTO.PlaceDTO savePlace(Long memberId, SavedPlaceReqDTO.SavedPlaceDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (savedPlaceRepository.isPlaceSavedInGroup(memberId, request.getPlaceId(), request.getGroupName())) {
            throw new MemberException(MemberErrorCode.SAVED_PLACE_ALREADY_EXISTS);
        }

        SavedPlace savedPlace = SavedPlaceConverter.toEntity(request, member);
        SavedPlace saved = savedPlaceRepository.save(savedPlace);

        return SavedPlaceConverter.toPlaceDTO(saved);
    }
}
