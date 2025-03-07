
package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
