package org.spring.attraction.dto;

import lombok.*;
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
}
