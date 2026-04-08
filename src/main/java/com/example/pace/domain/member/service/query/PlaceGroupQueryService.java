package com.example.pace.domain.member.service.query;

import com.example.pace.domain.member.converter.PlaceGroupConverter;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.repository.PlaceGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceGroupQueryService {
    private final PlaceGroupRepository placeGroupRepository;

    @Transactional(readOnly = true)
    public PlaceGroupResDTO.PlaceGroupListDTO getPlaceGroupList(Long memberId) {
        List<PlaceGroupResDTO.PlaceGroupQueryDTO> queryList = placeGroupRepository.findAllGroupInfoByMemberId(memberId);

        return PlaceGroupConverter.toPlaceGroupListDTO(queryList);
    }
}
