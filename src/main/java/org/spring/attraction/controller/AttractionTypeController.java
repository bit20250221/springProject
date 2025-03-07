package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AttractionTypeDto;
import org.spring.attraction.service.AttractionTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attractionType")
public class AttractionTypeController {
    private final AttractionTypeService attractionTypeService;

    @GetMapping("/save")
    public String save() {
        return "/attractionType/save";
    }

    @PostMapping("/save")
    public String save(AttractionTypeDto attractionTypeDto) {
        if(attractionTypeService.save(attractionTypeDto)){
            return "redirect:/attractionType/list";
        }
        return "redirect:/attractionType/save";
    }

    @GetMapping(value = {"/list", ""})
    public String list(Model model) {
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "/attractionType/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        attractionTypeService.delete(id);
        return "redirect:/attractionType/list";

    }


}
