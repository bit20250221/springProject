package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.PayType;
import org.spring.attraction.dto.*;
import org.spring.attraction.entity.Payment;
import org.spring.attraction.service.AttractionService;
import org.spring.attraction.service.PaymentService;
import org.spring.attraction.service.PaymentTypeService;
import org.spring.attraction.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final AttractionService attractionService;
    private final PaymentTypeService paymentTypeService;
    private final PaymentService paymentService;

    @GetMapping("/save/{id}")
    public String save(@PathVariable Long id, Model model) {
        AttractionDto attractionDto = attractionService.findById(id);
        List<PaymentTypeDto> paymentTypeDtoList = paymentTypeService.findAll();
        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("paymentTypeDtoList", paymentTypeDtoList);
        return "reservation/save";
    }

    @PostMapping("/save")
    public String save(AttractionDto attractionDto, Model model) {
        ReservationDto reservationDto = reservationService.save(attractionDto);
        return "redirect:/reservation/detail/" + reservationDto.getId();
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ReservationDto reservationDto = reservationService.findById(id);
        AttractionDto attractionDto = attractionService.findById(reservationDto.getAttraction().getId());
        PaymentDto paymentDto = paymentService.findById(reservationDto.getPaymentId());
        List<PaymentTypeDto> paymentTypeDtoList = paymentTypeService.findAll();

        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("reservationDto", reservationDto);
        model.addAttribute("paymentDto", paymentDto);
        model.addAttribute("paymentTypeDtoList", paymentTypeDtoList);
        return "reservation/detail";
    }

    @PostMapping("/update")
    public String update(ReservationUpdateDto reservationUpdateDto, Model model) {
        reservationService.update(reservationUpdateDto);
        return "redirect:/reservation/detail/" + reservationUpdateDto.getId();

    }

    @GetMapping("/list")
    public String list(Model model) {
        List<ViewReservationDto> viewReservationDtoList = reservationService.findAllByView();
        model.addAttribute("viewReservationDtoList", viewReservationDtoList);
        return "reservation/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", reservationService.delete(id));
        return "redirect:/reservation/list";

    }

}
