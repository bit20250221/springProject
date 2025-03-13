package org.spring.attraction.dto;

import jakarta.persistence.*;
import lombok.*;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.Payment;
import org.spring.attraction.entity.Reservation;
import org.spring.attraction.entity.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;
    private LocalDateTime createdate;
    private int peplenum;
    private LocalDateTime reservedate;
    private User user;
    private Attraction attraction;
    private Long paymentId;

    public static ReservationDto toDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setCreatedate(reservation.getCreatedate());
        reservationDto.setPeplenum(reservation.getPeplenum());
        reservationDto.setReservedate(reservation.getReservedate());
        reservationDto.setUser(reservation.getUser());
        reservationDto.setAttraction(reservation.getAttraction());
        return reservationDto;
    }


}
