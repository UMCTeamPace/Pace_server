package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.exception.PlaceGroupSuccessCode;
import com.example.pace.domain.member.service.PlaceGroupCommandService;
import com.example.pace.domain.member.service.PlaceGroupQueryService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class PlaceGroupController implements PlaceGroupControllerDocs {
    private final PlaceGroupCommandService placeGroupCommandService;
    private final PlaceGroupQueryService placeGroupQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PlaceGroupResDTO.PlaceGroupDTO> createPlaceGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PlaceGroupReqDTO.SaveGroupReqDTO request
    ) {
        Long memberId = userDetails.member().getId();

        return ApiResponse.onSuccess(
                PlaceGroupSuccessCode.PLACE_GROUP_CREATE_OK,
                placeGroupCommandService.createGroup(memberId, request)
        );
    }

    @GetMapping
    public ApiResponse<PlaceGroupResDTO.PlaceGroupListDTO> getPlaceGroupList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.member().getId();

        return ApiResponse.onSuccess(
                PlaceGroupSuccessCode.PLACE_GROUP_FOUND_OK,
                placeGroupQueryService.getPlaceGroupList(memberId)
        );
    }

    @PatchMapping("/{groupId}")
    public ApiResponse<PlaceGroupResDTO.PlaceGroupDTO> updatePlaceGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long groupId,
            @RequestBody @Valid PlaceGroupReqDTO.UpdateGroupReqDTO request
    ) {
        Long memberId = userDetails.member().getId();

        return ApiResponse.onSuccess(
                PlaceGroupSuccessCode.PLACE_GROUP_UPDATE_OK,
                placeGroupCommandService.updateGroup(memberId, groupId, request)
        );
    }
}
