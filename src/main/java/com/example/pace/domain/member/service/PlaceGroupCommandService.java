package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.PlaceGroupConverter;
import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.PlaceGroup;
import com.example.pace.domain.member.exception.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.exception.PlaceGroupErrorCode;
import com.example.pace.domain.member.exception.PlaceGroupException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.PlaceGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceGroupCommandService {
    private final PlaceGroupRepository placeGroupRepository;
    private final MemberRepository memberRepository;

    public PlaceGroupResDTO.PlaceGroupDTO createGroup(Long memberId, PlaceGroupReqDTO.SaveGroupReqDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 유저가 이미 같은 이름의 그룹을 가지고 있는지 확인
        if (placeGroupRepository.existsByMemberIdAndGroupName(memberId, request.getGroupName())) {
            throw new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_DUPLICATE_GROUP_NAME);
        }

        PlaceGroup placeGroup = PlaceGroupConverter.toEntity(request, member);

        return PlaceGroupConverter.toPlaceGroupDTO(placeGroupRepository.save(placeGroup));
    }
}
