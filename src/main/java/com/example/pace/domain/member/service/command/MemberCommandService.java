package com.example.pace.domain.member.service.command;

import com.example.pace.domain.member.dto.response.KakaoUserInfoResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.enums.Role;
import com.example.pace.domain.member.enums.SocialProvider;
import com.example.pace.domain.member.exception.code.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.global.util.JwtUtil;
import com.example.pace.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    // 회원 탈퇴 로직
    @Transactional
    public void withdrawalMember(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateRefreshToken(null);
        setTokenBlackList(accessToken);

        memberRepository.deleteById(member.getId());
    }

    // 로그아웃 처리
    @Transactional
    public void logout(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateRefreshToken(null);
        setTokenBlackList(accessToken);
    }

    @Transactional
    public Member getOrSaveMember(KakaoUserInfoResDTO kakaoUserInfoResDTO) {
        String socialId = kakaoUserInfoResDTO.getId().toString();

        return memberRepository.findBySocialProviderAndSocialIdIgnoreStatus(
                SocialProvider.KAKAO.name(),
                socialId
        ).orElseGet(() -> {
            Member newMember = Member.builder()
                    .socialId(socialId)
                    .email(kakaoUserInfoResDTO.getKakaoAccount().getEmail())
                    .nickname(kakaoUserInfoResDTO.getKakaoAccount().getProfile().getNickname())
                    .role(Role.ROLE_INCOMPLETE_USER)
                    .socialProvider(SocialProvider.KAKAO)
                    .isActive(true)
                    .build();

            return memberRepository.save(newMember);
        });
    }

    @Transactional
    public void updateRefreshToken(Long memberId, String refreshToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateRefreshToken(refreshToken);
    }

    public void setTokenBlackList(String accessToken) {
        Long remainingTime = jwtUtil.getExpirationTime(accessToken);
        redisUtil.setBlackList(accessToken, remainingTime);
    }
}
