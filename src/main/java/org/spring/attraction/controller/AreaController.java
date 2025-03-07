package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AreaMessage;
import org.spring.attraction.dto.AreaDto;
import org.spring.attraction.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @GetMapping(value = {"", "/", "/index", "/main"})
    public String area() {
        return "/area/main";
    }

    @GetMapping("/save")
    public String save() {
        return "/area/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AreaDto areaDto, RedirectAttributes redirectAttributes) {
        AreaMessage result = areaService.save(areaDto);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        if(result.getId() < 0) {
            redirectAttributes.addFlashAttribute("areaDto", areaDto);
            return "redirect:/area/save";
        }
        return "redirect:/area/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<AreaDto> areaDtoList = areaService.findAll();
        model.addAttribute("areaDtoList", areaDtoList);
        return "/area/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            AreaDto areaDto = areaService.findById(id);
            if(areaDto != null) {
                model.addAttribute("areaDto", areaDto);
                return "/area/update";
            }
            return "redirect:/area/list";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/area/list";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute AreaDto areaDto, RedirectAttributes redirectAttributes) {
        AreaMessage result = areaService.save(areaDto);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        if(result.getId() < 0) {
            return "redirect:/area/update/" + areaDto.getId();
        }
        return "redirect:/area/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            AreaDto areaDto = areaService.findById(id);
            if(areaDto != null) {
                model.addAttribute("areaDto", areaDto);
                return "/area/detail";
            }
            return "redirect:/area/list";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/area/list";
        }

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AreaMessage result = areaService.delete(id);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/area/list";
    }

}
