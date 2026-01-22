package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.enums.SocialProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 모든 상태(활성, 탈퇴)의 회원을 조회
    // @SQLRestriction 을 우회하기 위해 네이티브 쿼리 사용
    @Query(value = "SELECT * FROM member WHERE social_provider = :socialProvider AND social_id = :socialId", nativeQuery = true)
    Optional<Member> findBySocialProviderAndSocialIdIgnoreStatus(
            @Param("socialProvider") String socialProvider,
            @Param("socialId") String socialId
    );

    @Query(value = "SELECT * FROM member WHERE id = :memberId", nativeQuery = true)
    Optional<Member> findByIdIgnoreStatus(@Param("memberId") Long memberId);
}
