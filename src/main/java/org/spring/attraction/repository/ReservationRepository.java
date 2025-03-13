package org.spring.attraction.repository;

import org.spring.attraction.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByAttractionId(Long id);

    // 사용자 로그인 ID로 예약 조회
    List<Reservation> findByUser_UserLoginId(String userLoginId);
}
