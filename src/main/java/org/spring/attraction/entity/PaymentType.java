package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.ENUM.PayType;

import java.util.List;

@Entity
@Table(name = "paymenttype")
@Getter
@Setter
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentTypeId")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayType type;

    @OneToMany(mappedBy = "paymentType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
