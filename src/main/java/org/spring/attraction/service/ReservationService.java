package org.spring.attraction.service;

import org.spring.attraction.dto.ReservationDto;
import org.spring.attraction.entity.Reservation;
import org.spring.attraction.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    // 사용자 로그인 ID로 예약 조회
    public List<ReservationDto> getReservationsByLoginId(String userLoginId) {
        List<Reservation> reservations = reservationRepository.findByUser_UserLoginId(userLoginId);
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDtos.add(ReservationDto.toDto(reservation)); // Entity -> DTO 변환
        }
        return reservationDtos; // DTO 리스트 반환
    }

    // 예약 취소 메소드
    public void cancelReservation(Long reservationId, String userLoginId) {
        // 예약 데이터 가져오기
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 현재 사용자가 예약한 내용인지 확인
        if (!reservation.getUser().getUserLoginId().equals(userLoginId)) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
        }
        reservationRepository.delete(reservation);
    }
}
