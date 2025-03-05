package org.spring.attraction.repository;

import org.spring.attraction.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
