package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.SavedPlace;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SavedPlaceConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static SavedPlace toEntity(
            SavedPlaceReqDTO.SavedPlaceDTO dto,
            Member member
    ) {
        return SavedPlace.builder()
                .placeId(dto.getPlaceId())
                .groupName(dto.getGroupName())
                .placeName(dto.getPlaceName())
                .member(member)
                .build();
    }

    public static SavedPlaceResDTO.PlaceDTO toPlaceDTO(SavedPlace savedPlace) {
        return SavedPlaceResDTO.PlaceDTO.builder()
                .placeId(savedPlace.getPlaceId())
                .groupName(savedPlace.getGroupName())
                .placeName(savedPlace.getPlaceName())
                .createdAt(savedPlace.getCreatedAt().format(formatter))
                .build();
    }

    public static SavedPlaceResDTO.PlaceListDTO toPlaceListDTO(List<SavedPlace> savedPlaceList) {
        List<SavedPlaceResDTO.PlaceDTO> placeDTOList = savedPlaceList.stream()
                .map(SavedPlaceConverter::toPlaceDTO).toList();

        return SavedPlaceResDTO.PlaceListDTO.builder()
                .placeDTOList(placeDTOList)
                .count(savedPlaceList.size())
                .build();
    }
}
