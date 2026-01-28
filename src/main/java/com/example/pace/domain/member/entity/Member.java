package com.example.pace.domain.member.entity;

import com.example.pace.domain.member.enums.Role;
import com.example.pace.domain.member.enums.SocialProvider;
import com.example.pace.domain.schedule.entity.Schedule;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "social_provider_id_unique",
                columnNames = {"socialProvider", "socialId"}
        )
})
@SQLDelete(sql = "UPDATE member SET is_active = false WHERE id = ?") // delete()시 hard delete 하는 것이 아닌 soft delete를 진행
@SQLRestriction("is_active = true") // 조회시 isActive 필드가 true인 데이터만 조회
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nickname;

    @Column(name = "social_id", nullable = false)
    private String socialId; // 확장성을 위해 String 타입으로 필드 선언

    @Column(name = "refresh_token")
    private String refreshToken;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(name = "social_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    //온보딩
    @Column(name = "onboarding_complete", nullable = false)
    @Builder.Default
    private Boolean onboardingComplete = false;

    public void updateOnboardingCompleted(boolean completed) {
        this.onboardingComplete = completed;
    }

    // 추후에 매핑 관계 반영 예정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Schedule> scheduleList = new ArrayList<>();

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // schedule 관계 매핑
    public void addSchedule(Schedule schedule) {
        this.scheduleList.add(schedule);
        schedule.setMember(this);
    }
}
