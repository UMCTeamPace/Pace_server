package com.example.pace.domain.auth.service;

import com.example.pace.domain.auth.converter.AuthConverter;
import com.example.pace.domain.auth.dto.response.AuthResDTO;
import com.example.pace.domain.auth.exception.AuthErrorCode;
import com.example.pace.domain.auth.exception.AuthException;
import com.example.pace.domain.member.dto.response.KakaoUserInfoResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.enums.Role;
import com.example.pace.domain.member.enums.SocialProvider;
import com.example.pace.domain.member.exception.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.global.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {
    private final MemberRepository memberRepository;
    private final KakaoApiService kakaoApiService;
    private final JwtUtil jwtUtil;

    public AuthResDTO.LoginResultDTO loginWithKakao(String kakaoAccessToken) {
        // 카카오 API로 사용자 정보 조회
        KakaoUserInfoResDTO kakaoUserInfoResDTO = kakaoApiService.getUserInfo(kakaoAccessToken);
        String email = kakaoUserInfoResDTO.getKakaoAccount().getEmail();
        String socialId = kakaoUserInfoResDTO.getId().toString();

        Optional<Member> memberOptional = memberRepository.findBySocialProviderAndSocialIdIgnoreStatus(
                SocialProvider.KAKAO.name(),
                socialId
        );

        // 값이 있을 경우(기존 회원일 경우)
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            // 조회된 회원이 탈퇴한 상태인지 확인
            if (!member.getIsActive()) {
                throw new MemberException(MemberErrorCode.MEMBER_NOT_ACTIVE);
            }

            String accessToken = jwtUtil.createAccessToken(member.getId(), Role.ROLE_USER);
            String refreshToken = jwtUtil.createRefreshToken(member.getId());
            member.updateRefreshToken(refreshToken);

            return AuthConverter.toExistingMemberDTO(member, accessToken, refreshToken);
        } else {
            // 신규 회원일 경우
            Member newMember = Member.builder()
                    .socialId(socialId)
                    .email(email)
                    .nickname(kakaoUserInfoResDTO.getKakaoAccount().getProfile().getNickname())
                    .role(Role.ROLE_INCOMPLETE_USER)
                    .socialProvider(SocialProvider.KAKAO)
                    .isActive(true)
                    .build();

            memberRepository.save(newMember);

            // 온보딩용 임시 토큰 발급(10분 유효)
            String tempToken = jwtUtil.createTempToken(newMember.getId());

            return AuthConverter.toNewMemberDTO(newMember, tempToken);
        }
    }

    // 리프레쉬 토큰으로 액세스 토큰 재발행 로직
    public AuthResDTO.LoginResultDTO reissueToken(String refreshToken) {
        // 리프레쉬 토큰 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        // 토큰에서 memberId 추출
        Claims claims = jwtUtil.getClaimsFromToken(refreshToken);
        Long memberId = Long.parseLong(claims.getSubject());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 데이터베이스에 저장된 리프레쉬 토큰과 일치하는지 확인
        if (!refreshToken.equals(member.getRefreshToken())) {
            // 토큰이 일치하지 않다면 다른 곳에서 이미 재발급에 사용되어 탈취 가능성 의심
            throw new AuthException(AuthErrorCode.TOKEN_INVALID);
        }

        // 새로운 액세스 토큰과 리프레쉬 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(member.getId(), Role.ROLE_USER);
        String newRefreshToken = jwtUtil.createRefreshToken(member.getId());

        member.updateRefreshToken(newRefreshToken);

        return AuthConverter.toExistingMemberDTO(member, newAccessToken, newRefreshToken);
    }

    // 로그아웃 처리
    public void logout(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateRefreshToken(null);
    }
}
