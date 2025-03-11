package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.entity.Attraction;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {
    private Long id;
    private String name;
    private double avgrate;
    private int price;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String explanation;
    private Long areaId;
    private String area;
    private List<Long> attractionTypeDtoIdList;
    private String attractionTypeDtoList;
    private int peplenum;
    private LocalDateTime reservedate;
    private Long paymentTypeId;
    private MultipartFile img;
    private String imgUrl;

    public static AttractionDto toDto(Attraction attraction) {
        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setId(attraction.getId());
        attractionDto.setName(attraction.getName());
        attractionDto.setAvgrate(attraction.getAvgrate());
        attractionDto.setPrice(attraction.getPrice());
        attractionDto.setOpenTime(attraction.getOpentime());
        attractionDto.setCloseTime(attraction.getClosetime());
        attractionDto.setExplanation(attraction.getExplanation());
        attractionDto.setAreaId(attraction.getArea().getId());
        return attractionDto;
    }

    public static AttractionMessage validate(AttractionDto attractionDto) {
        String attractionName = attractionDto.getName().trim();
        if(attractionName.isEmpty()) {
            return AttractionMessage.getTypeById(-5);
        }else if(attractionName.length() > 10) {
            return AttractionMessage.getTypeById(-6);
        }

        int attractionPrice = attractionDto.getPrice();
        if(attractionPrice < 0) {
            return AttractionMessage.getTypeById(-7);
        }else if(attractionPrice > 500000 || attractionPrice < 1000) {
            return AttractionMessage.getTypeById(-8);
        }

        List<Long> attractionTypeDtoIdList = attractionDto.getAttractionTypeDtoIdList();
        if(attractionTypeDtoIdList.isEmpty()) {
            return AttractionMessage.getTypeById(-9);
        }else if(attractionTypeDtoIdList.size() > 3) {
            return AttractionMessage.getTypeById(-10);
        }

        return null;
    }

}
