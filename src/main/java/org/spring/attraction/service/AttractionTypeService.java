package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AttractionTypeDto;
import org.spring.attraction.entity.AttractionType;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.AttractionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionTypeService {
    private final AttractionTypeRepository attractionTypeRepository;

    public boolean save(AttractionTypeDto attractionTypeDto) {
        AttractionType attractionType = AttractionType.toEntity(attractionTypeDto);
        attractionTypeRepository.save(attractionType);
        return true;
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

    public void delete(Long id) {
        attractionTypeRepository.deleteById(id);
    }
}
