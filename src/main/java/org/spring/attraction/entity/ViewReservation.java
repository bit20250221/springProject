package org.spring.attraction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(name = "viewreservation")
@Getter
@Setter
@Immutable
public class ViewReservation {
    @Id
    @Column
    private Long id;

    @Column
    private Long attractionId;

    @Column
    private String attractionName;

    @Column
    private int attractionPrice;

    @Column
    private int reservationPeplenum;

    @Column
    private String userLoginId;

    @Column
    private LocalDateTime ReservationReservedate;

    @Column
    private String paymentTypeType;

    @Column
    private int totalPrice;
}
