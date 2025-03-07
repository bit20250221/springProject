package org.spring.attraction.dto;


import lombok.*;
import org.spring.attraction.ENUM.PayType;
import org.spring.attraction.entity.Attraction;

import java.time.LocalDateTime;
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
    private String openTime;
    private String closeTime;
    private String explanation;
    private Long areaId;
    private String area;
    private List<Long> attractionTypeDtoIdList;
    private String attractionTypeDtoList;
    private int peplenum;
    private String reservedate;
    private Long paymentTypeId;

    public static AttractionDto toDto(Attraction attraction) {
        AttractionDto attractionDto = new AttractionDto();
        attractionDto.setId(attraction.getId());
        attractionDto.setName(attraction.getName());
        attractionDto.setAvgrate(attraction.getAvgrate());
        attractionDto.setPrice(attraction.getPrice());
        attractionDto.setOpenTime(String.valueOf(attraction.getOpentime()));
        attractionDto.setCloseTime(String.valueOf(attraction.getClosetime()));
        attractionDto.setExplanation(attraction.getExplanation());
        attractionDto.setAreaId(attraction.getArea().getId());
        return attractionDto;
    }
}