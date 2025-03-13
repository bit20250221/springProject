package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.PaymentTypeDto;
import org.spring.attraction.entity.PaymentType;
import org.spring.attraction.repository.PaymentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    public List<PaymentTypeDto> findAll() {
        List<PaymentType> paymentTypeList = paymentTypeRepository.findAll();
        List<PaymentTypeDto> paymentTypeDtoList = new ArrayList<>();
        for(PaymentType paymentType : paymentTypeList ) {
            PaymentTypeDto paymentTypeDto = PaymentTypeDto.toDto(paymentType);
            paymentTypeDtoList.add(paymentTypeDto);
        }
        return paymentTypeDtoList;
    }
}
