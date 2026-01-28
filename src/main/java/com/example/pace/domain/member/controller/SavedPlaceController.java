package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.exception.MemberSuccessCode;
import com.example.pace.domain.member.service.SavedPlaceCommandService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/places")
public class SavedPlaceController implements SavedPlaceControllerDocs {
    private final SavedPlaceCommandService savedPlaceCommandService;

    @Override
    @PostMapping("/saved")
    public ResponseEntity<ApiResponse<SavedPlaceResDTO.PlaceDTO>> savePlace(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid SavedPlaceReqDTO.SavedPlaceDTO request
    ) {
        Long memberId = userDetails.member().getId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.onSuccess(
                                MemberSuccessCode.SAVED_PLACE_CREATE_OK,
                                savedPlaceCommandService.savePlace(memberId, request)
                        )
                );
    }
}
