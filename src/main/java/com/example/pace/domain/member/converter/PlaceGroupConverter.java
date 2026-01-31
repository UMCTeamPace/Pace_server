package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.PlaceGroup;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceGroupConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PlaceGroup toEntity(PlaceGroupReqDTO.SaveGroupReqDTO dto, Member member) {
        return PlaceGroup.builder()
                .groupName(dto.getGroupName())
                .groupColor(dto.getGroupColor())
                .member(member)
                .build();
    }

    public static PlaceGroupResDTO.PlaceGroupDTO toPlaceGroupDTO(PlaceGroup group) {
        return PlaceGroupResDTO.PlaceGroupDTO.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupColor(group.getGroupColor())
                .createdAt(group.getCreatedAt().format(formatter))
                .build();
    }

    public static PlaceGroupResDTO.PlaceGroupListDTO toPlaceGroupListDTO(List<PlaceGroup> groupList) {
        List<PlaceGroupResDTO.PlaceGroupDTO> dtoList = groupList.stream()
                .map(PlaceGroupConverter::toPlaceGroupDTO).toList();

        return PlaceGroupResDTO.PlaceGroupListDTO.builder()
                .placeGroupList(dtoList)
                .listSize(dtoList.size())
                .build();
    }
}
