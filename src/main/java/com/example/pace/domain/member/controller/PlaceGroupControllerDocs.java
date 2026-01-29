package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.PlaceGroupReqDTO;
import com.example.pace.domain.member.dto.response.PlaceGroupResDTO;
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

@Tag(name = "Place Group API", description = "장소 그룹 관련 API")
public interface PlaceGroupControllerDocs {
    @Operation(summary = "장소 그룹 생성 API", description = "새로운 장소 그룹(예: 맛집, 집 등)을 생성합니다. 사용자별로 그룹 이름은 중복될 수 없습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "그룹 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "성공 예시", value = "{\"isSuccess\": true, \"code\": \"PLACE_GROUP_201_1\", \"message\": \"그룹 생성 성공\", \"result\": { \"groupId\": 1, \"groupName\": \"맛집\", \"groupColor\": \"#FF5733\", \"createdAt\": \"2024-01-29 22:00\" }}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "그룹 생성 실패 (중복된 이름 등)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "이름 중복 예시", value = "{\"isSuccess\":false, \"code\":\"PLACE_GROUP_400_1\", \"message\":\"이미 존재하는 그룹 이름입니다.\", \"result\":null}")
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
    ResponseEntity<ApiResponse<PlaceGroupResDTO.PlaceGroupDTO>> createPlaceGroup(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @RequestBody PlaceGroupReqDTO.SaveGroupReqDTO request
    );
}