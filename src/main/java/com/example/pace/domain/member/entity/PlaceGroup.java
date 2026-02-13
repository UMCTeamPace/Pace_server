package com.example.pace.domain.member.entity;

import com.example.pace.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "place_group",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_place_group_member_group_name",
                        columnNames = {"member_id", "group_name"}
                )
        },
        // 회원별 그룹 목록 조회를 위한 인덱스 칼럼 지정
        indexes = {
                @Index(name = "idx_place_group_member", columnList = "member_id")
        }
)
public class PlaceGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName; // 회의 때 확인됐던 그룹명

    @Column(name = "group_color", nullable = false)
    private String groupColor; // 16진수 색상 코드 값

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "placeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SavedPlace> savedPlaceList = new ArrayList<>();

    public void addSavedPlace(SavedPlace savedPlace) {
        savedPlaceList.add(savedPlace);
        savedPlace.setPlaceGroup(this);
    }

    public void updateGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void updateGroupColor(String groupColor) {
        this.groupColor = groupColor;
    }
}
