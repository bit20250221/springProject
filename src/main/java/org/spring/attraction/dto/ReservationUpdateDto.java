package org.spring.attraction.dto;

import lombok.*;

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

    public static String validate(ReservationUpdateDto reservationUpdateDto) {
        int peplenum = reservationUpdateDto.getPeplenum();
        if(peplenum == 0) {
            return "인원수가 입력되지 않았습니다..";
        }else if(peplenum < 0) {
            return "인원수는 음수를 입력할 수 없습니다.";
        }else if(peplenum > 10) {
            return "인원수는 1~10명까지 입력이 가능합니다.";
        }

        LocalDateTime reservedate = reservationUpdateDto.getReservedate();
        if(reservedate == null) {
            return "예약일자가 입력되지 않았습니다.";
        }else{
            LocalDateTime today = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(reservedate.toLocalDate(), today.toLocalDate());
            if(reservedate.isBefore(today)) {
                return "예약일자는 과거의 날짜로는 입력하실 수 없습니다.";
            }else if(daysBetween >= 60){
                return "예약일자는 최대 60일 이내까지 입력이 가능합니다.";
            }
        }

        Long paymentTypeId = reservationUpdateDto.getPaymentTypeId();
        if(paymentTypeId == null) {
            return "결제방법이 입력되지 않았습니다.";
        }

        return null;
    }
}
