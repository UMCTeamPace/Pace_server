package com.example.pace.domain.member.settings.entity;

import com.example.pace.domain.member.entity.Member;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "setting",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_setting_member",
                columnNames = "member_id"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Setting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "early_arrival_time", nullable = false)
    private int earlyArrivalTime;

    @Column(name = "is_noti_enabled", nullable = false)
    private boolean isNotiEnabled;

    @Column(name = "is_loc_enabled", nullable = false)
    private boolean isLocEnabled;

    public static Setting defaultOf(Member member) {
        return Setting.builder()
                .member(member)
                .earlyArrivalTime(20)
                .isNotiEnabled(true)
                .isLocEnabled(false)
                .build();
    }

    public void update(
            Integer earlyArrivalTime,
            Boolean isNotiEnabled,
            Boolean isLocEnabled
    ) {
        if (earlyArrivalTime != null) this.earlyArrivalTime = earlyArrivalTime;
        if (isNotiEnabled != null) this.isNotiEnabled = isNotiEnabled;
        if (isLocEnabled != null) this.isLocEnabled = isLocEnabled;
    }
}

