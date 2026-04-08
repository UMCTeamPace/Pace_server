package com.example.pace.domain.member.controller.docs;

import com.example.pace.domain.member.dto.request.SettingUpdateRequestDTO;
import com.example.pace.domain.member.dto.response.SettingResponseDTO;
import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Settings", description = "사용자 설정 API")
public interface SettingControllerDocs {

    @Operation(summary = "내 설정 조회", description = "로그인한 사용자의 설정 값을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SettingResponseDTO.class),
                    examples = @ExampleObject(
                            name = "성공 응답 예시",
                            value = """
                                    {
                                      "isSuccess": true,
                                      "code": "SETTING200_0",
                                      "message": "설정 조회 성공",
                                      "result": {
                                        "isReminderActive": true,
                                        "earlyArrivalTime": 60,
                                        "calendarType": "GOOGLE",
                                        "alarms": [
                                          { "type": "SCHEDULE", "minutes": [5] },
                                          { "type": "DEPARTURE", "minutes": [10] }
                                        ]
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<SettingResponseDTO> getMySetting(@AuthenticationPrincipal CustomUserDetails user);

    @Operation(summary = "내 설정 수정", description = "로그인한 사용자의 알림/캘린더 설정을 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SettingResponseDTO.class),
                    examples = @ExampleObject(
                            name = "성공 응답 예시",
                            value = """
                                    {
                                      "isSuccess": true,
                                      "code": "SETTING200_1",
                                      "message": "설정 수정 성공",
                                      "result": {
                                        "isReminderActive": true,
                                        "earlyArrivalTime": 60,
                                        "calendarType": "GOOGLE",
                                        "alarms": [
                                          { "type": "SCHEDULE", "minutes": [5, 10080] },
                                          { "type": "DEPARTURE", "minutes": [5, 10, 15] }
                                        ]
                                      }
                                    }
                                    """
                    )
            )
    )
    ApiResponse<SettingResponseDTO> updateMySetting(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "설정 수정 요청 예시",
                                    value = """
                                            {
                                              "isReminderActive": true,
                                              "earlyArrivalTime": 60,
                                              "calendarType": "GOOGLE",
                                              "alarms": [
                                                { "type": "SCHEDULE",  "minutes": [] },
                                                { "type": "DEPARTURE", "minutes": [] }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            SettingUpdateRequestDTO request
    );
}
