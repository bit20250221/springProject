package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionTypeListMessage;
import org.spring.attraction.dto.AttractionTypeListDto;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.AttractionType;
import org.spring.attraction.entity.AttractionTypeList;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.AttractionTypeListRepository;
import org.spring.attraction.repository.AttractionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionTypeListService {
    private final AttractionRepository attractionRepository;
    private final AttractionTypeRepository attractionTypeRepository;
    private final AttractionTypeListRepository attractionTypeListRepository;

    public String save(AttractionTypeListDto attractionTypeListDto) {
        AttractionTypeList attractionTypeList = new AttractionTypeList();
        Attraction attraction = attractionRepository.findById(attractionTypeListDto.getAttractionId()).orElse(null);
        if(attraction != null) {
            attractionTypeList.setAttraction(attraction);
        }else{
            return AttractionTypeListMessage.getMessageById(-1);
        }

        AttractionType attractionType = attractionTypeRepository.findById(attractionTypeListDto.getAttractionTypeId()).orElse(null);
        if(attractionType != null){
            attractionTypeList.setAttractionType(attractionType);
        }else{
            return AttractionTypeListMessage.getMessageById(-2);
        }

        attractionTypeListRepository.save(attractionTypeList);
        return AttractionTypeListMessage.getMessageById(1);
    }

    public List<AttractionTypeListDto> findByAttractionId(Long id) {
        List<AttractionTypeList> attractionTypeListList = attractionTypeListRepository.findByAttractionId(id);
        List<AttractionTypeListDto> attractionTypeListDtoList = new ArrayList<>();
        for(AttractionTypeList attractionTypeList : attractionTypeListList) {
            AttractionTypeListDto attractionTypeListDto = AttractionTypeListDto.toEntity(attractionTypeList);
            attractionTypeListDto.setAttractionId(attractionTypeList.getAttraction().getId());
            attractionTypeListDto.setAttractionTypeId(attractionTypeList.getAttractionType().getId());
            attractionTypeListDtoList.add(attractionTypeListDto);
        }
        return attractionTypeListDtoList;
    }
}
