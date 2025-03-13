package org.spring.attraction.repository;

import org.spring.attraction.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByAttractionId(Long id);
}
