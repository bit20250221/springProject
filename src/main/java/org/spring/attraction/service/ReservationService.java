package org.spring.attraction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AttractionDto;
import org.spring.attraction.dto.PaymentDto;
import org.spring.attraction.dto.ReservationDto;
import org.spring.attraction.dto.ReservationUpdateDto;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.Payment;
import org.spring.attraction.entity.Reservation;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.PaymentRepository;
import org.spring.attraction.repository.ReservationRepository;
import org.spring.attraction.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ReservationDto save(AttractionDto attractionDto) {
        try{
            Reservation reservation = new Reservation();
            User user = userRepository.findById(1L).orElse(null);
            Attraction attraction = attractionRepository.findById(attractionDto.getId()).orElse(null);
            if(user == null || attraction == null) {
                return null;
            }

            reservation.setCreatedate(LocalDateTime.now());
            reservation.setPeplenum(attractionDto.getPeplenum());
            reservation.setReservedate(stringToLocalDateTime(attractionDto.getReservedate()));
            reservation.setUser(user);
            reservation.setAttraction(attraction);
            user.getReservations().add(reservation);
            attraction.getReservations().add(reservation);

            reservationRepository.save(reservation);

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setReservationId(reservation.getId());
            paymentDto.setPaymentTypeId(attractionDto.getPaymentTypeId());
            paymentDto.setCreatedate(LocalDateTime.now());
            Payment payment = paymentService.save(paymentDto);
            reservation.getPayments().add(payment);

            return ReservationDto.toDto(reservation);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalTime localTime = LocalTime.parse(date, formatter);
        return LocalDateTime.of(LocalDate.now(), localTime);
    }

    public ReservationDto findById(Long id) {

        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if(reservation != null) {
            ReservationDto reservationDto = ReservationDto.toDto(reservation);
            Payment payment = paymentRepository.findByReservationId(reservation.getId());
            reservationDto.setPaymentId(payment.getId());
            return reservationDto;
        }
        return null;
    }

    @Transactional
    public boolean update(ReservationUpdateDto reservationUpdateDto) {
        Reservation reservation = reservationRepository.findById(reservationUpdateDto.getId()).orElse(null);
        if(reservation != null) {
            reservation.setPeplenum(reservationUpdateDto.getPeplenum());
            System.out.println(reservationUpdateDto.getReservedate());
            reservation.setReservedate(reservationUpdateDto.getReservedate());

            Payment payment = paymentService.update(reservationUpdateDto);
            if(payment != null) {
                reservation.getPayments().add(payment);
            }
        }
        return false;
    }
}
