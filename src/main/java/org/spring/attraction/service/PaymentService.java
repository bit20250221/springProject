package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.PaymentDto;
import org.spring.attraction.dto.PaymentTypeDto;
import org.spring.attraction.dto.ReservationUpdateDto;
import org.spring.attraction.entity.Payment;
import org.spring.attraction.entity.PaymentType;
import org.spring.attraction.entity.Reservation;
import org.spring.attraction.repository.PaymentRepository;
import org.spring.attraction.repository.PaymentTypeRepository;
import org.spring.attraction.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public Payment save(PaymentDto paymentDto) {
        Payment payment = new Payment();
        PaymentType paymentType = paymentTypeRepository.findById(paymentDto.getPaymentTypeId()).orElse(null);
        Reservation reservation = reservationRepository.findById(paymentDto.getReservationId()).orElse(null);

        if (paymentType != null && reservation != null) {
            payment.setPaymentType(paymentType);
            payment.setCreatedate(paymentDto.getCreatedate());
            payment.setReservation(reservation);

            return paymentRepository.save(payment);
        }
        return null;
    }


    public PaymentDto findById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            PaymentDto paymentDto = PaymentDto.toDto(payment);
            return paymentDto;
        }
        return null;
    }

    public Payment update(ReservationUpdateDto reservationUpdateDto) {
        Payment payment = paymentRepository.findByReservationId(reservationUpdateDto.getId());
        if (payment != null) {
            PaymentType paymentType = paymentTypeRepository.findById(reservationUpdateDto.getPaymentTypeId()).orElse(null);
            Reservation reservation = reservationRepository.findById(reservationUpdateDto.getId()).orElse(null);
            if (paymentType != null && reservation != null) {
                payment.setPaymentType(paymentType);
                payment.setReservation(reservation);
                return paymentRepository.save(payment);

            }
            return null;
        }
        return null;
    }
}
