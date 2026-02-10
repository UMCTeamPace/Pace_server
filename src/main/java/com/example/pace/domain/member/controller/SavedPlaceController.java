package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.controller.docs.SavedPlaceControllerDocs;
import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.domain.member.enums.SavedPlaceSortType;
import com.example.pace.domain.member.exception.code.SavedPlaceSuccessCode;
import com.example.pace.domain.member.service.command.SavedPlaceCommandService;
import com.example.pace.domain.member.service.query.SavedPlaceQueryService;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class SavedPlaceController implements SavedPlaceControllerDocs {
    private final SavedPlaceCommandService savedPlaceCommandService;
    private final SavedPlaceQueryService savedPlaceQueryService;

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
                                SavedPlaceSuccessCode.SAVED_PLACE_CREATE_OK,
                                savedPlaceCommandService.savePlace(memberId, request)
                        )
                );
    }

    @GetMapping("/saved/{groupId}")
    public ApiResponse<SavedPlaceResDTO.PlaceListDTO> getSavedPlaceList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "LATEST") SavedPlaceSortType sortType
    ) {
        Long memberId = userDetails.member().getId();
        return ApiResponse.onSuccess(
                SavedPlaceSuccessCode.SAVED_PLACE_FOUND_OK,
                savedPlaceQueryService.getSavedPlaceList(memberId, groupId, sortType)
        );
    }

    @Override
    @DeleteMapping("/saved")
    public ApiResponse<String> deletePlaces(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SavedPlaceReqDTO.DeletePlaceListDTO request
    ) {
        Long memberId = userDetails.member().getId();
        savedPlaceCommandService.deletePlaces(memberId, request.getPlaceIdList());

        return ApiResponse.onSuccess(
                SavedPlaceSuccessCode.SAVED_PLACE_DELETE_OK,
                "장소들이 성공적으로 삭제되었습니다."
        );
    }

    @PatchMapping("/saved/move")
    public ApiResponse<String> movePlaces(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid SavedPlaceReqDTO.MovePlaceListDTO request
    ) {
        Long memberId = userDetails.member().getId();
        savedPlaceCommandService.movePlaces(memberId, request);

        return ApiResponse.onSuccess(
                SavedPlaceSuccessCode.SAVED_PLACE_MOVE_OK,
                SavedPlaceSuccessCode.SAVED_PLACE_MOVE_OK.getMessage()
        );
    }
}
