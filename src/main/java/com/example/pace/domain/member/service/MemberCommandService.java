package com.example.pace.domain.member.service;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.exception.MemberErrorCode;
import com.example.pace.domain.member.exception.MemberException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.global.util.JwtUtil;
import com.example.pace.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    // 회원 탈퇴 로직
    public void withdrawalMember(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateRefreshToken(null);

        Long remainingTime = jwtUtil.getExpirationTime(accessToken);

        if (remainingTime > 0) {
            redisUtil.setBlackList(accessToken, remainingTime);
        }

        memberRepository.deleteById(member.getId());
    }

    // 로그아웃 처리
    public void logout(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateRefreshToken(null);

        Long remainingTime = jwtUtil.getExpirationTime(accessToken);

        // 남은 시간이 0보다 클 때만 저장
        if (remainingTime > 0) {
            redisUtil.setBlackList(accessToken, remainingTime);
        }
    }
}
