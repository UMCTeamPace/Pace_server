package com.example.pace.domain.auth.service.command;

import com.example.pace.domain.auth.converter.AuthConverter;
import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.auth.exception.code.AuthErrorCode;
import com.example.pace.domain.auth.exception.AuthException;
import com.example.pace.domain.auth.service.query.KakaoApiQueryService;
import com.example.pace.domain.member.dto.response.KakaoUserInfoResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.enums.Role;
import com.example.pace.domain.member.exception.code.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.service.command.MemberCommandService;
import com.example.pace.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthCommandService {
    private final MemberRepository memberRepository;
    private final KakaoApiQueryService kakaoApiQueryService;
    private final MemberCommandService memberCommandService;
    private final JwtUtil jwtUtil;

    public AuthResDTO.LoginResultDTO loginWithKakao(String kakaoAccessToken) {
        // 카카오 API로 사용자 정보 조회
        KakaoUserInfoResDTO kakaoUserInfoResDTO = kakaoApiQueryService.getUserInfo(kakaoAccessToken);

        Member member = memberCommandService.getOrSaveMember(kakaoUserInfoResDTO);

        // 비활성된(탈퇴) 회원인 경우
        if (!member.getIsActive()) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_ACTIVE);
        }

        // 온보딩 미완료 회원일 경우
        if (member.getRole() == Role.ROLE_INCOMPLETE_USER) {
            String tempToken = jwtUtil.createTempToken(member.getId());

            return AuthConverter.toNewMemberDTO(member, tempToken);
        }

        String accessToken = jwtUtil.createAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtUtil.createRefreshToken(member.getId());

        // 리프레쉬 토큰 업데이트
        memberCommandService.updateRefreshToken(member.getId(), refreshToken);

        return AuthConverter.toExistingMemberDTO(member, accessToken, refreshToken);
    }

    // 리프레쉬 토큰으로 액세스 토큰 재발행 로직
    @Transactional
    public AuthResDTO.LoginResultDTO reissueToken(String refreshToken) {
        // 리프레쉬 토큰 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        // 토큰에서 클레임 정보 추출
        Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
        String category = claims.get("category", String.class);

        if (category == null || !category.equals("refresh")) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        Long memberId = Long.parseLong(claims.getSubject());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 온보딩 미완료 회원은 토큰 재발행 불가
        if (member.getRole() == Role.ROLE_INCOMPLETE_USER) {
            throw new MemberException(MemberErrorCode.MEMBER_UNAUTHORIZED);
        }

        // 데이터베이스에 저장된 리프레쉬 토큰과 일치하는지 확인
        if (!refreshToken.equals(member.getRefreshToken())) {
            // 토큰이 일치하지 않다면 다른 곳에서 이미 재발급에 사용되어 탈취 가능성 의심
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        // 새로운 액세스 토큰과 리프레쉬 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(member.getId(), member.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(member.getId());

        member.updateRefreshToken(newRefreshToken);

        return AuthConverter.toExistingMemberDTO(member, newAccessToken, newRefreshToken);
    }
}
