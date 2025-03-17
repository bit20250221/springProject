package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.dto.*;
import org.spring.attraction.repository.AttractionImgRepository;
import org.spring.attraction.service.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final AttractionImgService attractionImgService;
    private final UserService userService;

    @GetMapping(value = {"", "/", "/index", "/main"})
    public String attraction(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        return "attraction/main";
    }

    @GetMapping("/save")
    public String save(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "attraction/save";
    }

    @PostMapping("/save")
    public String save(AttractionDto attractionDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        attractionDto.setUserDetails(userDetails);
        AttractionMessage result = attractionService.save(attractionDto);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        if(result.getId() < 0){
            redirectAttributes.addFlashAttribute("areaDto", attractionDto);
            return "redirect:/attraction/save";
        }
        return "redirect:/attraction/list";
    }

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<AttractionDto> attractionDtoList = attractionService.findAll();
//        model.addAttribute("attractionDtoList", attractionDtoList);
//        return "/attraction/list";
//    }

    @GetMapping("/list")
    public String list(@PageableDefault(page = 1) Pageable pageable, Model model,
                       @RequestParam(required = false) Integer type, @RequestParam(required = false) String search,
                       @AuthenticationPrincipal UserDetails userDetails) {
        String userRole = userService.getUserRole(userDetails);
        model.addAttribute("userRole", userRole);
        model.addAttribute("userAttractionId", userService.getAttractionId(userDetails));
        Page<ViewAttractionDto> viewAttractionDtoPage = new PageImpl<>(new ArrayList<>());
        if(search != null && type != null){
            if(type == 1){
                viewAttractionDtoPage = attractionService.findViewByName(pageable, search);
            }else if(type == 2){
                viewAttractionDtoPage = attractionService.findViewByType(pageable, search);
            }else if(type == 3){
                viewAttractionDtoPage = attractionService.findViewByArea(pageable, search);
            }else{
                viewAttractionDtoPage = attractionService.findViewAll(pageable);
            }
        } else{
            viewAttractionDtoPage = attractionService.findViewAll(pageable);
        }
        int blockLimit = 10;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < viewAttractionDtoPage.getTotalPages()) ? startPage + blockLimit - 1 : viewAttractionDtoPage.getTotalPages();


        model.addAttribute("viewAttractionDtoPage", viewAttractionDtoPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "attraction/list";
    }

    @GetMapping("/detail/{id}")
    public String list(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        AttractionDto attractionDto = attractionService.findById(id);
        List<AttractionTypeListDto> attractionTypeListDtoList = attractionTypeListService.findByAttractionId(id);
        List<Long> selectedAttractionTypeIdList = new ArrayList<>();
        for(AttractionTypeListDto attractionTypeListDto : attractionTypeListDtoList){
            selectedAttractionTypeIdList.add(attractionTypeListDto.getAttractionTypeId());
        }
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();

        AttractionImgDto attractionImgDto = attractionImgService.findByAttractionId(id);
        if(attractionImgDto != null){
            String attractionImgUrl = "/files/" + attractionImgDto.getUUID() + "_" + attractionImgDto.getName();

            model.addAttribute("attractionImgUrl", attractionImgUrl);
        }

        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("selectedAttractionTypeIdList", selectedAttractionTypeIdList);
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "attraction/detail";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        AttractionDto attractionDto = attractionService.findById(id);
        List<AttractionTypeListDto> attractionTypeListDtoList = attractionTypeListService.findByAttractionId(id);
        List<Long> selectedAttractionTypeIdList = new ArrayList<>();
        for(AttractionTypeListDto attractionTypeListDto : attractionTypeListDtoList){
            selectedAttractionTypeIdList.add(attractionTypeListDto.getAttractionTypeId());
        }
        List<AreaDto> areaDtoList = areaService.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = attractionTypeService.findAll();
        AttractionImgDto attractionImgDto = attractionImgService.findByAttractionId(id);
        if(attractionImgDto != null){
            String attractionImgUrl = "/files/" + attractionImgDto.getUUID() + "_" + attractionImgDto.getName();

            model.addAttribute("attractionImgUrl", attractionImgUrl);
        }

        model.addAttribute("attractionDto", attractionDto);
        model.addAttribute("selectedAttractionTypeIdList", selectedAttractionTypeIdList);
        model.addAttribute("areaDtoList", areaDtoList);
        model.addAttribute("attractionTypeDtoList", attractionTypeDtoList);
        return "attraction/update";
    }

    @PostMapping("/update")
    public String update(AttractionDto attractionDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        attractionDto.setUserDetails(userDetails);
        AttractionMessage result = attractionService.update(attractionDto);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/attraction/update/" + attractionDto.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userRole", userService.getUserRole(userDetails));
        AttractionMessage result = attractionService.delete(id);
        redirectAttributes.addFlashAttribute("message", result.getMessage());
        return "redirect:/attraction/list";

    }
}
