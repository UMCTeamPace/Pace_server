package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
import com.example.pace.domain.member.exception.PlaceGroupSuccessCode;
import com.example.pace.domain.member.service.PlaceGroupCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class PlaceGroupController implements PlaceGroupControllerDocs {
    private final PlaceGroupCommandService placeGroupCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<PlaceGroupResDTO.PlaceGroupDTO>> createPlaceGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PlaceGroupReqDTO.SaveGroupReqDTO request
    ) {
        Long memberId = userDetails.member().getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.onSuccess(
                                PlaceGroupSuccessCode.PLACE_GROUP_CREATE_OK,
                                placeGroupCommandService.createGroup(memberId, request)
                        )
                );
    }
}
