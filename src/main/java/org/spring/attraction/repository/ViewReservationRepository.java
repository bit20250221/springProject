package org.spring.attraction.repository;

import org.spring.attraction.entity.ViewReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewReservationRepository extends JpaRepository<ViewReservation, Long> {
    List<ViewReservation> findByUserLoginId(String userLoginId);
}
