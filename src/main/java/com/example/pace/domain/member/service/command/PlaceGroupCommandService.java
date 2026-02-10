package com.example.pace.domain.member.service.command;

import com.example.pace.domain.member.converter.PlaceGroupConverter;
import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.PlaceGroup;
import com.example.pace.domain.member.exception.code.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.exception.code.PlaceGroupErrorCode;
import com.example.pace.domain.member.exception.PlaceGroupException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.PlaceGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceGroupCommandService {
    private final PlaceGroupRepository placeGroupRepository;
    private final MemberRepository memberRepository;

    // 그룹 저장
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

    // 그룹 업데이트
    public PlaceGroupResDTO.PlaceGroupDTO updateGroup(Long memberId, Long groupId,
                                                      PlaceGroupReqDTO.UpdateGroupReqDTO request) {
        PlaceGroup placeGroup = placeGroupRepository.findByMemberIdAndId(memberId, groupId)
                .orElseThrow(() -> new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_NOT_FOUND));

        String newName = request.getGroupName();

        // 그룹 이름 수정 시 예외 케이스에 맞추어 업데이트 하는 로직
        if (newName != null && !newName.isBlank() && !newName.equals(placeGroup.getGroupName())) {
            // 중복 체크
            if (placeGroupRepository.existsByMemberIdAndGroupName(memberId, newName)) {
                throw new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_DUPLICATE_GROUP_NAME);
            }
            placeGroup.updateGroupName(newName);
        }

        // 그룹 컬러 수정 로직
        String newColor = request.getGroupColor();

        if (newColor != null && !newColor.isBlank() && !newColor.equals(placeGroup.getGroupColor())) {
            placeGroup.updateGroupColor(newColor);
        }

        return PlaceGroupConverter.toPlaceGroupDTO(placeGroup);
    }

    // 그룹 삭제
    public void deleteGroups(Long memberId, List<Long> groupIdList) {
        List<PlaceGroup> groupList = placeGroupRepository.findAllByIdInAndMemberId(groupIdList, memberId);

        if (groupList.size() != groupIdList.size()) {
            throw new PlaceGroupException(PlaceGroupErrorCode.PLACE_GROUP_UNAUTHORIZED);
        }

        placeGroupRepository.deleteAll(groupList);
    }
}
