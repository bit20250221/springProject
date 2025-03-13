package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.PaymentType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeDto {
    private Long id;
    private String type;

    public static PaymentTypeDto toDto(PaymentType paymentType) {
        PaymentTypeDto paymentTypeDto = new PaymentTypeDto();
        paymentTypeDto.setId(paymentType.getId());
        paymentTypeDto.setType(paymentType.getType().name());
        return paymentTypeDto;
    }
}
