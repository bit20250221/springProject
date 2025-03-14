package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.ENUM.AttractionTypeMessage;
import org.spring.attraction.dto.AttractionTypeDto;
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
public class AttractionTypeService {
    private final AttractionTypeRepository attractionTypeRepository;
    private final AttractionTypeListRepository attractionTypeListRepository;

    public AttractionTypeMessage save(AttractionTypeDto attractionTypeDto) {
        AttractionTypeMessage result =  AttractionTypeDto.validate(attractionTypeDto);
        if(result != null) {
            return result;
        }
        AttractionType attractionType = attractionTypeRepository.findByType(attractionTypeDto.getType()).orElse(null);
        if(attractionType == null) {
            attractionTypeRepository.save(AttractionType.toEntity(attractionTypeDto));
            return AttractionTypeMessage.getTypeById(1);
        }
        return AttractionTypeMessage.getTypeById(-1);
    }

    public List<AttractionTypeDto> findAll() {
        List<AttractionType> attractionTypeList = attractionTypeRepository.findAll();
        List<AttractionTypeDto> attractionTypeDtoList = new ArrayList<>();
        if (!attractionTypeList.isEmpty()) {
            for(AttractionType attractionType : attractionTypeList) {
                AttractionTypeDto attractionTypeDto = AttractionTypeDto.toDto(attractionType);
                attractionTypeDtoList.add(attractionTypeDto);
            }
            return attractionTypeDtoList;
        }
        return null;
    }

    public AttractionTypeMessage delete(Long id) {
        List<AttractionTypeList> attractionTypeListList = attractionTypeListRepository.findByAttractionTypeId(id);
        if(!attractionTypeListList.isEmpty()) {
            return AttractionTypeMessage.getTypeById(-2);
        }
        attractionTypeRepository.deleteById(id);

        return AttractionTypeMessage.getTypeById(2);
    }
}
