package com.example.pace.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResultDTO {
        private Boolean isNewUser;
    }

    // 기존 회원 로그인 시 응답 dto
    @Getter
    @NoArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL) // null이 아닌 필드에만 json에 포함
    public static class ExistingUserDTO extends LoginResultDTO {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
        private String nickname;
        private String email;

        @Builder
        public ExistingUserDTO(Long memberId, String accessToken, String refreshToken, String nickname, String email) {
            super(false); // 기존 회원으므로 isNewUser는 항상 false
            this.memberId = memberId;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.nickname = nickname;
            this.email = email;
        }
    }

    // 신규 회원 가입 시 응답 dto
    @Getter
    @NoArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class NewUserDTO extends LoginResultDTO {
        private Long memberId;
        private String tempToken;
        private String email;

        @Builder
        public NewUserDTO(Long memberId, String tempToken, String email) {
            super(true); // 신규 회원이므로 항상 true
            this.memberId = memberId;
            this.tempToken = tempToken;
            this.email = email;
        }
    }
}
