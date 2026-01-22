package com.example.pace.domain.schedule.repository;

import com.example.pace.domain.schedule.entity.Route;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
