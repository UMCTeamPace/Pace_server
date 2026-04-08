package com.example.pace.global.auth;

import com.example.pace.global.util.JwtUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 개발자 전용 인증 토큰 발급기(?)
@Disabled
@SpringBootTest
public class JwtTokenGeneratorTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("개발자용 테스트 토큰 생성")
    void generateDevToken() {
        Long testMemberId = 1L;

        String token = jwtUtil.createDevToken(testMemberId);

        System.out.println("토큰을 생성합니다.");
        System.out.println("Token: " + token);
    }
}
