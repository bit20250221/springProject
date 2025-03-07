package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.ENUM.ReservationMessage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateDto {
    private Long id;
    private int peplenum;
    private LocalDateTime reservedate;
    private Long paymentTypeId;

    public static ReservationMessage validate(ReservationUpdateDto reservationUpdateDto) {
        int peplenum = reservationUpdateDto.getPeplenum();
        if(peplenum == 0) {
            return ReservationMessage.getTypeById(-5);
        }else if(peplenum < 0) {
            return ReservationMessage.getTypeById(-6);
        }else if(peplenum > 10) {
            return ReservationMessage.getTypeById(-7);
        }

        LocalDateTime reservedate = reservationUpdateDto.getReservedate();
        if(reservedate == null) {
            return ReservationMessage.getTypeById(-8);
        }else{
            LocalDateTime today = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(reservedate.toLocalDate(), today.toLocalDate());
            if(reservedate.isBefore(today)) {
                return ReservationMessage.getTypeById(-9);
            }else if(daysBetween >= 60){
                return ReservationMessage.getTypeById(-10);
            }
        }

        Long paymentTypeId = reservationUpdateDto.getPaymentTypeId();
        if(paymentTypeId == null) {
            return ReservationMessage.getTypeById(-11);
        }

        return null;
    }
}
