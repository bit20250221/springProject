package org.spring.attraction.dto;


import lombok.*;
import org.spring.attraction.entity.ViewReservation;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ViewReservationDto {
    private Long id;
    private String attractionName;
    private int attractionPrice;
    private int reservationPeplenum;
    private String userLoginId;
    private LocalDateTime ReservationReservedate;
    private String paymentTypeType;
    private int totalPrice;

    public static ViewReservationDto toDto(ViewReservation viewReservation) {
        ViewReservationDto viewReservationDto = new ViewReservationDto();
        viewReservationDto.setId(viewReservation.getId());
        viewReservationDto.setAttractionName(viewReservation.getAttractionName());
        viewReservationDto.setAttractionPrice(viewReservation.getAttractionPrice());
        viewReservationDto.setReservationPeplenum(viewReservation.getReservationPeplenum());
        viewReservationDto.setUserLoginId(viewReservation.getUserLoginId());
        viewReservationDto.setReservationReservedate(viewReservation.getReservationReservedate());
        viewReservationDto.setPaymentTypeType(viewReservation.getPaymentTypeType());
        viewReservationDto.setTotalPrice(viewReservation.getTotalPrice());
        return viewReservationDto;
    }
}
