package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.PlaceGroupConverter;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.entity.PlaceGroup;
import com.example.pace.domain.member.repository.PlaceGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceGroupQueryService {
    private final PlaceGroupRepository placeGroupRepository;

    public PlaceGroupResDTO.PlaceGroupListDTO getPlaceGroupList(Long memberId) {
        List<PlaceGroup> groupList = placeGroupRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        return PlaceGroupConverter.toPlaceGroupListDTO(groupList);
    }
}
