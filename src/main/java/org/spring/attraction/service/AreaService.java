package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AreaMessage;
import org.spring.attraction.dto.AreaDto;
import org.spring.attraction.entity.Area;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.repository.AreaRepository;
import org.spring.attraction.repository.AttractionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaRepository areaRepository;
    private final AttractionRepository attractionRepository;

    public AreaMessage save(AreaDto areaDto) {
        AreaMessage result = AreaDto.validate(areaDto);
        if(result != null) {
            return result;
        }
        List<Area> areaList = areaRepository.findByCountryAndCity(areaDto.getCountry(), areaDto.getCity());
        if(areaList.isEmpty()) {
            areaRepository.save(Area.toAreaEntity(areaDto));
            return AreaMessage.getTypeById(1);
        }
        return AreaMessage.getTypeById(-1);
    }

    public List<AreaDto> findAll() {
        List<Area> areaList = areaRepository.findAll();
        if(!areaList.isEmpty()) {
            List<AreaDto> areaDtoList = new ArrayList<>();
            for (Area area : areaList) {
                AreaDto areaDto = AreaDto.toDto(area);
                areaDtoList.add(areaDto);
            }
            return areaDtoList;
        }
        return null;
    }

    public AreaDto findById(Long id) {
        try{
            Area optionalArea = areaRepository.findById(id).orElse(null);
            if(optionalArea != null) {
                return AreaDto.toDto(optionalArea);
            }
            return null;
        }catch (Exception e) {
            throw new IllegalStateException("오류가 발생하였습니다.");
        }
    }

    public AreaMessage delete(Long id) {
        List<Attraction> attraction = attractionRepository.findByAreaId(id);
        if(!attraction.isEmpty()) {
            return AreaMessage.getTypeById(-2);
        }
        areaRepository.deleteById(id);
        return AreaMessage.getTypeById(2);
    }


}
