package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.Payment;
import org.spring.attraction.entity.PaymentType;
import org.spring.attraction.entity.Reservation;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private LocalDateTime createdate;
    private Long paymentTypeId;
    private Long reservationId;


    public static PaymentDto toDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setCreatedate(payment.getCreatedate());
        paymentDto.setPaymentTypeId(payment.getPaymentType().getId());
        paymentDto.setReservationId(payment.getReservation().getId());
        return paymentDto;
    }
}