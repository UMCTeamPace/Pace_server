package com.example.pace.domain.member.controller.docs;

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
import org.springframework.web.bind.annotation.PathVariable;
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
    ApiResponse<PlaceGroupResDTO.PlaceGroupDTO> createPlaceGroup(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @RequestBody PlaceGroupReqDTO.SaveGroupReqDTO request
    );

    @Operation(summary = "장소 그룹 목록 조회 API", description = "사용자의 모든 장소 그룹 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "그룹 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "성공 예시", value = "{\"isSuccess\": true, \"code\": \"PLACE_GROUP_200_1\", \"message\": \"그룹 목록 조회 성공\", \"result\": { \"placeGroupList\": [ { \"groupId\": 1, \"groupName\": \"맛집\", \"groupColor\": \"#FF5733\", \"createdAt\": \"2024-01-29 22:00\" }, { \"groupId\": 2, \"groupName\": \"집\", \"groupColor\": \"#0000FF\", \"createdAt\": \"2024-01-29 23:00\" } ], \"listSize\": 2 }}")
                    )
            )
    })
    ApiResponse<PlaceGroupResDTO.PlaceGroupListDTO> getPlaceGroupList(
            @Parameter(hidden = true) CustomUserDetails userDetails
    );

    @Operation(summary = "장소 그룹 수정 API", description = "장소 그룹의 이름이나 색상을 수정합니다. 변경할 필드만 보내면 되며, 이름 변경 시 중복 체크가 수행됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "그룹 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "성공 예시", value = "{\"isSuccess\": true, \"code\": \"PLACE_GROUP_200_2\", \"message\": \"그룹 수정 성공\", \"result\": { \"groupId\": 1, \"groupName\": \"변경된 맛집\", \"groupColor\": \"#00FF00\", \"createdAt\": \"2024-01-29 22:00\" }}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "그룹 수정 실패 (중복된 이름)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "이름 중복 예시", value = "{\"isSuccess\":false, \"code\":\"PLACE_GROUP_400_1\", \"message\":\"이미 존재하는 그룹 이름입니다.\", \"result\":null}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "그룹을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Not Found 예시", value = "{\"isSuccess\":false, \"code\":\"PLACE_GROUP_404_1\", \"message\":\"존재하지 않는 그룹입니다.\", \"result\":null}")
                    )
            )
    })
    ApiResponse<PlaceGroupResDTO.PlaceGroupDTO> updatePlaceGroup(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "수정할 그룹 ID", required = true) @PathVariable Long groupId,
            @RequestBody PlaceGroupReqDTO.UpdateGroupReqDTO request
    );

    @Operation(summary = "장소 그룹 다중 삭제 API", description = "장소 그룹들을 ID 리스트를 통해 한 번에 삭제합니다. 그룹 삭제 시 내부에 저장된 장소들도 함께 삭제됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "그룹 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "삭제 성공 예시", value = "{\"isSuccess\":true, \"code\":\"PLACE_GROUP_200_3\", \"message\":\"그룹들이 성공적으로 삭제되었습니다.\", \"result\":\"그룹들이 성공적으로 삭제되었습니다.\"}")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "그룹을 찾을 수 없거나 삭제 권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "에러 예시", value = "{\"isSuccess\":false, \"code\":\"PLACE_GROUP_401_1\", \"message\":\"삭제할 권한이 없거나 존재하지 않는 그룹입니다.\", \"result\":null}")
                    )
            )
    })
    ApiResponse<String> deleteGroups(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @RequestBody PlaceGroupReqDTO.DeleteGroupListReqDTO request
    );
}