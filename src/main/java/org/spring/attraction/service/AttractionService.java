package org.spring.attraction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.dto.*;
import org.spring.attraction.entity.*;
import org.spring.attraction.repository.*;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;
    private final AreaRepository areaRepository;
    private final AttractionTypeListService attractionTypeListService;
    private final AttractionTypeRepository attractionTypeRepository;
    private final AttractionTypeListRepository attractionTypeListRepository;
    private final ReservationRepository reservationRepository;
    private final AttractionImgRepository attractionImgRepository;
    private final AttractionImgService attractionImgService;
    private final ViewAttractionRepository viewAttractionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public AttractionMessage save(AttractionDto attractionDto) {
        AttractionMessage result = AttractionDto.validate(attractionDto);
        if (result != null) {
            return result;
        }
        Area area = areaRepository.findById(attractionDto.getAreaId()).orElse(null);
        if (area == null) {
            return AttractionMessage.getTypeById(-1);
        }

        Attraction attraction = Attraction.toAttractionEntity(attractionDto);
        attraction.setArea(area);
        Attraction ResultAttraction = attractionRepository.save(attraction);
        for(Long attractionTypeDtoId : attractionDto.getAttractionTypeDtoIdList()) {
            AttractionType attractionType = attractionTypeRepository.findById(attractionTypeDtoId).orElse(null);
            if (attractionType == null) {
                return AttractionMessage.getTypeById(-2);
            }
            AttractionMessage result2 = attractionTypeListService.save(new AttractionTypeListDto(null, ResultAttraction.getId(), attractionTypeDtoId));
            if (result2.getId() < 0) {
                return result2;
            }
        }

        if(attractionDto.getImg() != null) {
            try{
                AttractionImg attractionImg = attractionImgService.save(attractionDto.getImg());
                attractionImg.setAttraction(attraction);
                attractionImgRepository.save(attractionImg);
            }catch (Exception e){
                e.printStackTrace();
                return AttractionMessage.getTypeById(-11);
            }

        }

        User user = userService.getUser(attractionDto.getUserDetails());
        if(user != null) {
            user.setAttraction(attraction);
        }else{
            return AttractionMessage.getTypeById(-16);
        }

        return AttractionMessage.getTypeById(1);
    }

    public List<AttractionDto> findAll() {
        List<Attraction> attractionList = attractionRepository.findAll();
        List<AttractionDto> attractionDtoList = new ArrayList<>();

        for (Attraction attraction : attractionList) {
            AttractionDto attractionDto = AttractionDto.toDto(attraction);
            attractionDto.setOpenTime(attractionDto.getOpenTime());
            attractionDto.setCloseTime(attractionDto.getCloseTime());

            List<AttractionTypeList> attractionTypeLists = attractionTypeListRepository.findByAttractionId(attractionDto.getId());
            List<String> attractionTypeStringList = new ArrayList<>();

            for(AttractionTypeList attractionTypeList : attractionTypeLists) {
                Optional<AttractionType> attractionType = attractionTypeRepository.findById(attractionTypeList.getAttractionType().getId());

                if(attractionType.isPresent()) {
                    AttractionTypeDto attractionTypeDto = AttractionTypeDto.toDto(attractionType.get());
                    attractionTypeStringList.add(attractionTypeDto.getType());
                }
            }
            attractionDto.setAttractionTypeDtoList(attractionTypeStringList.toString());

            Optional<Area> area = areaRepository.findById(attractionDto.getAreaId());

            if(area.isPresent()) {

                AreaDto areaDto = AreaDto.toDto(area.get());
                attractionDto.setArea(areaDto.getCountry() + " " + areaDto.getCity());
            }

            attractionDtoList.add(attractionDto);
        }

        return attractionDtoList;
    }

    public AttractionDto findById(Long id) {
        Optional<Attraction> attraction = attractionRepository.findById(id);
        if(attraction.isPresent()) {
            AttractionDto attractionDto = AttractionDto.toDto(attraction.get());
            attractionDto.setOpenTime(attractionDto.getOpenTime());
            attractionDto.setCloseTime(attractionDto.getCloseTime());
            return attractionDto;
        }
        return null;
    }

    @Transactional
    public AttractionMessage update(AttractionDto attractionDto) {
        AttractionMessage result = AttractionDto.validate(attractionDto);
        if (result != null) {
            return result;
        }

        Attraction attraction = attractionRepository.findById(attractionDto.getId()).orElse(null);
        if(attraction != null) {
            attraction.setName(attractionDto.getName());
            attraction.setPrice(attractionDto.getPrice());
            attraction.setOpentime(attractionDto.getOpenTime());
            attraction.setClosetime(attractionDto.getCloseTime());
            attraction.setExplanation(attractionDto.getExplanation());
            attraction.setArea(areaRepository.findById(attractionDto.getAreaId()).orElse(null));

        }else{
            return AttractionMessage.getTypeById(-3);
        }

        attraction.getAttractionsTypeLists().clear();

        for(Long id : attractionDto.getAttractionTypeDtoIdList()) {
            AttractionType attractionType = attractionTypeRepository.findById(id).orElse(null);
            if(attractionType != null) {
                AttractionTypeList attractionTypeList = attractionTypeListRepository.findByAttractionIdAndAttractionTypeId(attraction.getId(), attractionType.getId()).orElse(null);
                if(attractionTypeList == null) {
                    AttractionTypeList newAttractionTypeList = new AttractionTypeList();
                    newAttractionTypeList.setAttraction(attraction);
                    newAttractionTypeList.setAttractionType(attractionType);
                    attraction.getAttractionsTypeLists().add(newAttractionTypeList);
                    attractionType.getAttractionTypeListSet().add(newAttractionTypeList);
                }
            }else {
                return AttractionMessage.getTypeById(-2);
            }
        }

        if(attractionDto.getImg() != null && !attractionDto.getImg().isEmpty()) {

            AttractionImg findAttractionImg = attractionImgRepository.findByAttractionId(attractionDto.getId()).orElse(null);
            if(findAttractionImg != null) {
                AttractionImgDto attractionImgDto = new AttractionImgDto();
                attractionImgDto.setAttractionId(attractionDto.getId());
                attractionImgDto.setImg(attractionDto.getImg());

                AttractionMessage result2 = attractionImgService.update(attractionImgDto);
                if(result2 != null) {
                    return result2;
                }
            }else{
                try{
                    AttractionImg saveAttractionImg = attractionImgService.save(attractionDto.getImg());
                    saveAttractionImg.setAttraction(attraction);
                }catch (Exception e){
                    e.printStackTrace();
                    return AttractionMessage.getTypeById(-11);
                }
            }

        }

        return AttractionMessage.getTypeById(2);
    }

    @Transactional
    public AttractionMessage delete(Long id) {
        List<Reservation> reservationList = reservationRepository.findByAttractionId(id);
        if(!reservationList.isEmpty()) {
            return AttractionMessage.getTypeById(-4);
        }

        Attraction attraction = attractionRepository.findById(id).orElse(null);
        if(attraction == null) {
            return AttractionMessage.getTypeById(-3);
        }

        AttractionImg attractionImg = attractionImgRepository.findByAttractionId(attraction.getId()).orElse(null);
        if(attractionImg != null) {
            AttractionMessage result = attractionImgService.delete(attraction.getId());
            if(result != null) {
                return result;
            }
        }

        attraction.getAttractionsTypeLists().clear();
        User user = userRepository.findByAttraction(attraction);
        if(user == null) {
            return AttractionMessage.getTypeById(-16);
        }

        user.setAttraction(null);
        attractionRepository.delete(attraction);

        return AttractionMessage.getTypeById(3);

    }

    public Page<ViewAttractionDto> findViewAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<ViewAttraction> viewAttractionPage = viewAttractionRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<ViewAttractionDto> viewAttractionDtoPage = viewAttractionPage.
                map(viewAttraction ->
                        new ViewAttractionDto(
                                viewAttraction.getId(), viewAttraction.getName(), viewAttraction.getAvgrate(),
                                viewAttraction.getPrice(), viewAttraction.getOpenTime(), viewAttraction.getCloseTime(),
                                viewAttraction.getType(), viewAttraction.getArea()));

        return viewAttractionDtoPage;

    }

    public Page<ViewAttractionDto> findViewByName(Pageable pageable, String search) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<ViewAttraction> viewAttractionPage = viewAttractionRepository.findByNameContaining(search, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<ViewAttractionDto> viewAttractionDtoPage = viewAttractionPage.
                map(viewAttraction ->
                        new ViewAttractionDto(
                                viewAttraction.getId(), viewAttraction.getName(), viewAttraction.getAvgrate(),
                                viewAttraction.getPrice(), viewAttraction.getOpenTime(), viewAttraction.getCloseTime(),
                                viewAttraction.getType(), viewAttraction.getArea()));

        return viewAttractionDtoPage;
    }

    public Page<ViewAttractionDto> findViewByType(Pageable pageable, String search) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<ViewAttraction> viewAttractionPage = viewAttractionRepository.findByTypeContaining(search, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<ViewAttractionDto> viewAttractionDtoPage = viewAttractionPage.
                map(viewAttraction ->
                        new ViewAttractionDto(
                                viewAttraction.getId(), viewAttraction.getName(), viewAttraction.getAvgrate(),
                                viewAttraction.getPrice(), viewAttraction.getOpenTime(), viewAttraction.getCloseTime(),
                                viewAttraction.getType(), viewAttraction.getArea()));

        return viewAttractionDtoPage;
    }

    public Page<ViewAttractionDto> findViewByArea(Pageable pageable, String search) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Page<ViewAttraction> viewAttractionPage = viewAttractionRepository.findByAreaContaining(search, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<ViewAttractionDto> viewAttractionDtoPage = viewAttractionPage.
                map(viewAttraction ->
                        new ViewAttractionDto(
                                viewAttraction.getId(), viewAttraction.getName(), viewAttraction.getAvgrate(),
                                viewAttraction.getPrice(), viewAttraction.getOpenTime(), viewAttraction.getCloseTime(),
                                viewAttraction.getType(), viewAttraction.getArea()));

        return viewAttractionDtoPage;
    }
}
