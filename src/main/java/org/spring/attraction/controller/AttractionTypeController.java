package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionTypeMessage;
import org.spring.attraction.dto.AttractionTypeDto;
import org.spring.attraction.service.AttractionTypeService;
import org.spring.attraction.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/attractionType")
public class AttractionTypeController {
    private final AttractionTypeService attractionTypeService;
    private final UserService userService;

    @GetMapping("/save")
    public String save(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        return "/attractionType/save";
    }

    @PostMapping("/save")
    public String save(AttractionTypeDto attractionTypeDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        AttractionTypeMessage result = attractionTypeService.save(attractionTypeDto);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        if(result.getId() < 0){
            return "redirect:/attractionType/save";
        }
        return "redirect:/attractionType/list";
    }

    @GetMapping(value = {"/list", ""})
    public String list(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "/attractionType/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        AttractionTypeMessage result = attractionTypeService.delete(id);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/attractionType/list";
    }


}
