package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SavedPlaceConverter;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.entity.SavedPlace;
import com.example.pace.domain.member.exception.SavedPlaceErrorCode;
import com.example.pace.domain.member.exception.SavedPlaceException;
import com.example.pace.domain.member.repository.SavedPlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedPlaceQueryService {
    private final SavedPlaceRepository savedPlaceRepository;

    public SavedPlaceResDTO.PlaceListDTO getSavedPlaceList(Long memberId, String groupName) {
        if (groupName == null) {
            throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_NOT_EXISTS_GROUP_NAME);
        }

        List<SavedPlace> placeList = savedPlaceRepository.findAllPlaceByMemberAndGroupName(memberId, groupName);

        if (placeList.isEmpty()) {
            throw new SavedPlaceException(SavedPlaceErrorCode.SAVED_PLACE_NOT_FOUND);
        }
        return SavedPlaceConverter.toPlaceListDTO(placeList);
    }
}
