package org.spring.attraction.repository;

import org.spring.attraction.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByReservationId(Long id);
}
