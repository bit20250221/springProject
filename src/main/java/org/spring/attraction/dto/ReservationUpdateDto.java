package org.spring.attraction.dto;

import lombok.*;

import java.time.LocalDateTime;

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
}