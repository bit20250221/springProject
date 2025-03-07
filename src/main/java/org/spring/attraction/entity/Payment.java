package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.dto.PaymentDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdate;

    @ManyToOne
    @JoinColumn(name="paymentTypeId", nullable = false)
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name="reservationId", nullable = false)
    private Reservation reservation;

    public static Payment toEntity(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setId(paymentDto.getId());
        payment.setCreatedate(paymentDto.getCreatedate());
        payment.setPaymentType(paymentDto.getPaymentType());
        payment.setReservation(paymentDto.getReservation());
        return payment;
    }
}
