package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AreaDto;
import org.spring.attraction.dto.AttractionDto;
import org.spring.attraction.dto.AttractionTypeDto;
import org.spring.attraction.dto.AttractionTypeListDto;
import org.spring.attraction.entity.Area;
import org.spring.attraction.service.AreaService;
import org.spring.attraction.service.AttractionService;
import org.spring.attraction.service.AttractionTypeListService;
import org.spring.attraction.service.AttractionTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attraction")
@RequiredArgsConstructor
public class AttractionController {
    private final AreaService areaService;
    private final AttractionService attractionService;
    private final AttractionTypeService attractionTypeService;
    private final AttractionTypeListService attractionTypeListService;

    @GetMapping(value = {"", "/", "/index", "/main"})
    public String attraction() {
        return "/attraction/main";
    }

    @GetMapping("/save")
    public String save(Model model) {
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "/attraction/save";
    }

    @PostMapping("/save")
    public String save(AttractionDto attractionDto, RedirectAttributes redirectAttributes) {
        if(attractionService.save(attractionDto)){
            return "redirect:/attraction/list";
        }
        redirectAttributes.addFlashAttribute("areaDto", attractionDto);
        redirectAttributes.addFlashAttribute("message", "저장에 실패하였습니다.");
        return "redirect:/attraction/save";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<AttractionDto> attractionDtoList = attractionService.findAll();

        model.addAttribute("attractionDtoList", attractionDtoList);
        return "/attraction/list";
    }

    @GetMapping("/detail/{id}")
    public String list(@PathVariable Long id, Model model) {
        AttractionDto attractionDto = attractionService.findById(id);
        List<AttractionTypeListDto> attractionTypeListDtoList = attractionTypeListService.findByAttractionId(id);
        List<Long> selectedAttractionTypeIdList = new ArrayList<>();
        for(AttractionTypeListDto attractionTypeListDto : attractionTypeListDtoList){
            selectedAttractionTypeIdList.add(attractionTypeListDto.getAttractionTypeId());
        }
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();

        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("selectedAttractionTypeIdList", selectedAttractionTypeIdList);
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "/attraction/detail";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        AttractionDto attractionDto = attractionService.findById(id);
        List<AttractionTypeListDto> attractionTypeListDtoList = attractionTypeListService.findByAttractionId(id);
        List<Long> selectedAttractionTypeIdList = new ArrayList<>();
        for(AttractionTypeListDto attractionTypeListDto : attractionTypeListDtoList){
            selectedAttractionTypeIdList.add(attractionTypeListDto.getAttractionTypeId());
        }
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();

        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("selectedAttractionTypeIdList", selectedAttractionTypeIdList);
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "/attraction/update";
    }

    @PostMapping("/update")
    public String update(AttractionDto attractionDto, RedirectAttributes redirectAttributes) {
        System.out.println(attractionDto.getAttractionTypeDtoIdList());
        if(attractionService.update(attractionDto)){
            return "redirect:/attraction/list";
        }
        redirectAttributes.addFlashAttribute("areaDto", attractionDto);
        redirectAttributes.addFlashAttribute("message", "저장에 실패하였습니다.");
        return "redirect:/attraction/update/" + attractionDto.getId();
    }


    @GetMapping("/delete")
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", attractionService.delete(id));
        return "redirect:/attraction/list";

    }
}
