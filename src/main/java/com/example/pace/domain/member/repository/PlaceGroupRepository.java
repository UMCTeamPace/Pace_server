package com.example.pace.domain.member.repository;

import com.example.pace.domain.member.entity.PlaceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceGroupRepository extends JpaRepository<PlaceGroup, Long> {
}
