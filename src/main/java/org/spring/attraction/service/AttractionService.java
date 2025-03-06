package org.spring.attraction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AreaDto;
import org.spring.attraction.dto.AttractionDto;
import org.spring.attraction.dto.AttractionTypeDto;
import org.spring.attraction.dto.AttractionTypeListDto;
import org.spring.attraction.entity.*;
import org.spring.attraction.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionRepository attractionRepository;
    private final AreaRepository areaRepository;
    private final AttractionTypeListService attractionTypeListService;
    private final AttractionTypeRepository attractionTypeRepository;
    private final AttractionTypeListRepository attractionTypeListRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public boolean save(AttractionDto attractionDto) {
        Optional<Area> area = areaRepository.findById(attractionDto.getAreaId());
        if (area.isPresent()) {
            Attraction attraction = Attraction.toAttractionEntity(attractionDto);
            attraction.setOpentime(stringToLocalDateTime(attractionDto.getOpenTime()));
            attraction.setClosetime(stringToLocalDateTime(attractionDto.getCloseTime()));
            attraction.setArea(area.get());
            Attraction ResultAttraction = attractionRepository.save(attraction);
            for(Long attractionTypeDtoId : attractionDto.getAttractionTypeDtoIdList()) {
                attractionTypeListService.save(new AttractionTypeListDto(
                        null, ResultAttraction.getId(), attractionTypeDtoId
                ));
            }
            return true;
        }
        return false;
    }


    public static LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(date, formatter);
        return LocalDateTime.of(LocalDate.now(), localTime);
    }

    public List<AttractionDto> findAll() {
        List<Attraction> attractionList = attractionRepository.findAll();
        List<AttractionDto> attractionDtoList = new ArrayList<>();

        for (Attraction attraction : attractionList) {
            AttractionDto attractionDto = AttractionDto.toDto(attraction);
            attractionDto.setOpenTime(attractionDto.getOpenTime().substring(Math.max(0, attractionDto.getOpenTime().length() - 5)));
            attractionDto.setCloseTime(attractionDto.getCloseTime().substring(Math.max(0, attractionDto.getCloseTime().length() - 5)));

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
            attractionDto.setOpenTime(attractionDto.getOpenTime().substring(Math.max(0, attractionDto.getOpenTime().length() - 5)));
            attractionDto.setCloseTime(attractionDto.getCloseTime().substring(Math.max(0, attractionDto.getCloseTime().length() - 5)));
            return attractionDto;
        }
        return null;
    }

    @Transactional
    public boolean update(AttractionDto attractionDto) {
        try{
            Attraction attraction = attractionRepository.findById(attractionDto.getId()).orElse(null);
            if(attraction != null) {
                attraction.setName(attractionDto.getName());
                attraction.setPrice(attractionDto.getPrice());
                attraction.setOpentime(stringToLocalDateTime(attractionDto.getOpenTime()));
                attraction.setClosetime(stringToLocalDateTime(attractionDto.getCloseTime()));
                attraction.setExplanation(attractionDto.getExplanation());
                attraction.setArea(areaRepository.findById(attractionDto.getAreaId()).orElse(null));

            }else{
                return false;
            }
            attraction.getAttractionsTypeLists().clear();

            if(attractionDto.getAttractionTypeDtoIdList() == null){
                return false;
            }
            for(Long id : attractionDto.getAttractionTypeDtoIdList()) {
                AttractionType attractionType = attractionTypeRepository.findById(id).orElse(null);
                if(attractionType != null) {
                    AttractionTypeList attractionTypeList = attractionTypeListRepository.findByAttractionIdAndAttractionTypeId(attraction.getId(), attractionType.getId());
                    if(attractionTypeList == null) {
                        AttractionTypeList newAttractionTypeList = new AttractionTypeList();
                        newAttractionTypeList.setAttraction(attraction);
                        newAttractionTypeList.setAttractionType(attractionType);
                        attraction.getAttractionsTypeLists().add(newAttractionTypeList);
                        attractionType.getAttractionTypeListSet().add(newAttractionTypeList);
                    }
                }else {
                    return false;
                }
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public String delete(Long id) {
        List<Reservation> reservationList = reservationRepository.findByAttractionId(id);
        if(reservationList != null) {
            return "예약된 정보가 있어 삭제할 수 없습니다.";
        }


        return null;
    }
}
