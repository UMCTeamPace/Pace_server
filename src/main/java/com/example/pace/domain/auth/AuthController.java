package com.example.pace.domain.auth;

import com.example.pace.global.apiPayload.ApiResponse;
import com.example.pace.global.apiPayload.code.GeneralSuccessCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/kakao")
    public ApiResponse<String> kakaoLogin(
            @RequestBody String testMethod
    ) {
        return ApiResponse.onSuccess(
                GeneralSuccessCode.OK,
                "테스트입니다."
        );
    }
}
