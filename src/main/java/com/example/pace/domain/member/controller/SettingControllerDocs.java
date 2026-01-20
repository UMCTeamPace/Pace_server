package com.example.pace.domain.member.controller;

import com.example.pace.domain.member.dto.request.SettingUpdateRequest;
import com.example.pace.domain.member.dto.response.SettingResponse;
import com.example.pace.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Settings", description = "사용자 설정 API")
public interface SettingControllerDocs {

    @Operation(summary = "설정 조회", description = "특정 memberId의 설정 값을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SettingResponse.class),
                    examples = @ExampleObject(
                            name = "성공 응답 예시",
                            value = """
                                    {
                                      "isSuccess": true,
                                      "code": "SETTING200_0",
                                      "message": "설정 조회 성공",
                                      "result": {
                                        "alarmEnabled": true,
                                        "reminderTimes": [5, 10, 15, 30, 60]
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<SettingResponse> getMySetting(@RequestParam Long memberId);

    @Operation(summary = "설정 수정", description = "알림 설정을 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SettingResponse.class),
                    examples = @ExampleObject(
                            name = "성공 응답 예시",
                            value = """
                                    {
                                      "isSuccess": true,
                                      "code": "SETTING200_1",
                                      "message": "설정 수정 성공",
                                      "result": {
                                        "alarmEnabled": true,
                                        "reminderTimes": [10, 30]
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<SettingResponse> updateMySetting(
            @RequestParam Long memberId,
            @RequestBody SettingUpdateRequest request
    );
}
