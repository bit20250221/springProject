package org.spring.attraction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.ReservationMessage;
import org.spring.attraction.dto.*;
import org.spring.attraction.entity.*;
import org.spring.attraction.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final ViewReservationRepository viewReservationRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public List<ViewReservationDto> findAllByView() {
        List<ViewReservation> viewReservationList = viewReservationRepository.findAll();
        List<ViewReservationDto> viewReservationDtoList = new ArrayList<>();
        for (ViewReservation viewReservation : viewReservationList) {
            ViewReservationDto viewReservationDto = ViewReservationDto.toDto(viewReservation);
            viewReservationDtoList.add(viewReservationDto);
        }
        return viewReservationDtoList;
    }

    @Transactional
    public ReservationMessage save(AttractionDto attractionDto) {
        ReservationUpdateDto reservationUpdateDto = new ReservationUpdateDto();
        reservationUpdateDto.setPeplenum(attractionDto.getPeplenum());
        reservationUpdateDto.setReservedate(attractionDto.getReservedate());
        reservationUpdateDto.setPaymentTypeId(attractionDto.getPaymentTypeId());

        ReservationMessage result = ReservationUpdateDto.validate(reservationUpdateDto);
        if (result != null) {
            return result;
        }

        User user = userRepository.findById(4L).orElse(null);
        if(user == null) {
            return ReservationMessage.getTypeById(-1);
        }

        Attraction attraction = attractionRepository.findById(attractionDto.getId()).orElse(null);
        if(attraction == null) {
            return ReservationMessage.getTypeById(-2);
        }

        PaymentType paymentType = paymentTypeRepository.findById(attractionDto.getPaymentTypeId()).orElse(null);
        if(paymentType == null) {
            return ReservationMessage.getTypeById(-3);
        }

        Reservation reservation = new Reservation();
        reservation.setCreatedate(LocalDateTime.now());
        reservation.setPeplenum(attractionDto.getPeplenum());
        reservation.setReservedate(attractionDto.getReservedate());
        reservation.setUser(user);
        reservation.setAttraction(attraction);
        user.getReservations().add(reservation);
        attraction.getReservations().add(reservation);

        reservationRepository.save(reservation);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setReservation(reservation);
        paymentDto.setPaymentType(paymentType);
        paymentDto.setCreatedate(LocalDateTime.now());

        Payment payment = Payment.toEntity(paymentDto);
        reservation.getPayments().add(paymentRepository.save(payment));

        return ReservationMessage.getTypeById(1);

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
    public ReservationMessage update(ReservationUpdateDto reservationUpdateDto) {
        ReservationMessage result = ReservationUpdateDto.validate(reservationUpdateDto);
        if (result != null) {
            return result;
        }

        Reservation reservation = reservationRepository.findById(reservationUpdateDto.getId()).orElse(null);
        if(reservation != null) {
            reservation.setPeplenum(reservationUpdateDto.getPeplenum());
            System.out.println(reservationUpdateDto.getReservedate());
            reservation.setReservedate(reservationUpdateDto.getReservedate());

            Payment payment = paymentService.update(reservationUpdateDto);
            if(payment != null) {
                reservation.getPayments().add(payment);
                return ReservationMessage.getTypeById(2);
            }

            return ReservationMessage.getTypeById(-3);
        }
        return ReservationMessage.getTypeById(-4);
    }

    @Transactional
    public ReservationMessage delete(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if(reservation != null) {
            reservation.getPayments().clear();
            reservationRepository.delete(reservation);
            return ReservationMessage.getTypeById(3);
        }
        return ReservationMessage.getTypeById(-4);
    }
}
