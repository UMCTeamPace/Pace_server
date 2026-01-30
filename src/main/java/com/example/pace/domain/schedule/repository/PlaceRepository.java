package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}