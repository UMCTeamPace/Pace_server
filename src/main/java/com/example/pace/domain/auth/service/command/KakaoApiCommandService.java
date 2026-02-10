package com.example.pace.domain.auth.service.command;

import com.example.pace.domain.auth.exception.code.AuthErrorCode;
import com.example.pace.domain.member.dto.response.KakaoUserInfoResDTO;
import com.example.pace.global.apiPayload.exception.GeneralException;
import com.example.pace.global.config.KaKaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoApiCommandService {
    private final WebClient webClient;
    private final KaKaoProperties kakaoProperties;

    public KakaoUserInfoResDTO getUserInfo(String accessToken) {
        return webClient.get()
                .uri(kakaoProperties.getUserInfoUri()) // application.yaml에 설정된 uri
                .headers(h -> h.setBearerAuth(accessToken)) // 헤더 설정
                .retrieve() // 응답을 받아옴
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("카카오 API 클라이언트 에러. status: {}, body: {}", response.statusCode(), errorBody);
                            // 커스텀 예외로 전환하여 예외 발생
                            return Mono.error(new GeneralException(AuthErrorCode.KAKAO_4XX));
                        }))
                .bodyToMono(KakaoUserInfoResDTO.class)
                .block();
    }
}
