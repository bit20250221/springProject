package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.PayType;
import org.spring.attraction.dto.AttractionDto;
import org.spring.attraction.dto.PaymentTypeDto;
import org.spring.attraction.dto.ReservationDto;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final AttractionService attractionService;
    private final PaymentTypeService paymentTypeService;

    @GetMapping("/save/{id}")
    public String save(@PathVariable Long id, Model model) {
        AttractionDto attractionDto = attractionService.findById(id);
        List<PaymentTypeDto> paymentTypeDtoList = paymentTypeService.findAll();
        System.out.println(paymentTypeDtoList.toString());
        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("paymentTypeDtoList", paymentTypeDtoList);
        return "reservation/save";
    }

    @PostMapping("/save")
    public String save(AttractionDto attractionDto, Model model) {
        System.out.println(attractionDto.toString());
        ReservationDto reservationDto = reservationService.save(attractionDto);
        return "redirect:/reservation/detail" + reservationDto.getId();
    }

//    @GetMapping("/detail/{id}")
//    public String detail(@PathVariable Long id, Model model) {
//
//    }
}
