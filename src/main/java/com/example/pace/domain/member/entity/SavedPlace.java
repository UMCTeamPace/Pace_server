package com.example.pace.domain.member.entity;


import com.example.pace.domain.schedule.entity.RouteDetail;
import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "saved_place",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_saved_place_member_group",
                        columnNames = {"member_id", "group_name"} // 하나의 유저가 같은 이름의 그룹을 만들지 못하게 설정
                ),
                @UniqueConstraint(
                        name = "uk_saved_place_member_place",
                        columnNames = {"member_id", "place_id", "group_name"} // 하나의 유저가 같은 그룹 내에 같은 장소를 저장하지 못하게 설정
                )
        }
)
public class SavedPlace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_name", nullable = false)
    private String placeName; // 장소명

    @Column(name = "group_name", nullable = false)
    private String groupName; // 회의 때 확인됐던 그룹명

    @Column(name = "place_id")
    private String placeId; // 고유 장소 ID (예: 구글 플레이스 ID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
