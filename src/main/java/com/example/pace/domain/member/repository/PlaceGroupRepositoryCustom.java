package com.example.pace.domain.member.repository;


import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import java.util.List;

public interface PlaceGroupRepositoryCustom {
    List<PlaceGroupResDTO.PlaceGroupQueryDTO> findAllGroupInfoByMemberId(Long memberId);
}
