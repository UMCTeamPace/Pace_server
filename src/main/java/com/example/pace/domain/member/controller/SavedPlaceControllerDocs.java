package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.SavedPlaceReqDTO;
import com.example.pace.domain.member.dto.response.SavedPlaceResDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Saved Place API", description = "장소 저장(즐겨찾기) 관련 API")
public interface SavedPlaceControllerDocs {

    @Operation(summary = "장소 저장 API", description = "사용자가 특정 장소를 특정 그룹 내에 저장(즐겨찾기)합니다. 같은 그룹 내 중복된 장소는 저장이 불가합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "장소 저장 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "저장 성공 예시", value = "{\"isSuccess\":true, \"code\":\"SAVED_PLACE_201_1\", \"message\":\"장소 저장 성공\", \"result\":{\"savedPlaceId\":1, \"groupId\":10, \"placeName\":\"스타벅스 강남역점\", \"placeId\":\"21160611\", \"createdAt\":\"2024-01-21 16:00\"}}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "저장 실패 (중복 또는 유효하지 않은 그룹)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "중복 에러 예시", value = "{\"isSuccess\":false, \"code\":\"SAVED_PLACE_400_1\", \"message\":\"이미 해당 그룹에 저장된 장소입니다.\", \"result\":null}"),
                                    @ExampleObject(name = "그룹 없음 예시", value = "{\"isSuccess\":false, \"code\":\"PLACE_GROUP_404_1\", \"message\":\"존재하지 않는 그룹입니다.\", \"result\":null}")
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "인증 에러 예시", value = "{\"isSuccess\":false, \"code\":\"COMMON401\", \"message\":\"인증이 필요합니다.\", \"result\":null}")
                    )
            )
    })
    ResponseEntity<ApiResponse<SavedPlaceResDTO.PlaceDTO>> savePlace(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @RequestBody SavedPlaceReqDTO.SavedPlaceDTO request
    );

    @Operation(summary = "저장된 장소 조회 API", description = "사용자가 저장한 장소 목록을 특정 그룹별로 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "장소 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "조회 성공 예시", value = "{\"isSuccess\":true, \"code\":\"SAVED_PLACE_200_1\", \"message\":\"저장된 장소 조회 성공\", \"result\":{\"placeDTOList\":[{\"savedPlaceId\":1, \"groupId\":10, \"placeName\":\"스타벅스 강남역점\", \"placeId\":\"21160611\", \"createdAt\":\"2024-01-21 16:00\"}], \"count\":1}}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 그룹 ID 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "에러 예시", value = "{\"isSuccess\":false, \"code\":\"SAVED_PLACE_400_2\", \"message\":\"그룹 ID가 유효하지 않습니다.\", \"result\":null}")
                    )
            )
    })
    ApiResponse<SavedPlaceResDTO.PlaceListDTO> getSavedPlaceList(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "조회할 그룹 ID", required = true) Long groupId
    );
}