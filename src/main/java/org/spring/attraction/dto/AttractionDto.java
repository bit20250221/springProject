package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.entity.Attraction;

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

    public static String validate2(AttractionDto attractionDto) {
        int peplenum = attractionDto.getPeplenum();
        if(peplenum == 0) {
            return "인원수가 입력되지 않았습니다..";
        }else if(peplenum < 0) {
            return "인원수는 음수를 입력할 수 없습니다.";
        }else if(peplenum > 10) {
            return "인원수는 1~10명까지 입력이 가능합니다.";
        }

        LocalDateTime reservedate = attractionDto.getReservedate();
        if(reservedate == null) {
            return "예약일자가 입력되지 않았습니다.";
        }else{
            LocalDateTime today = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(reservedate.toLocalDate(), today.toLocalDate());
            if(reservedate.isBefore(today)) {
                return "예약일자는 과거의 날짜로는 입력하실 수 없습니다.";
            }else if(daysBetween >= 60){
                return "예약일자는 최대 60일 이내까지 입력이 가능합니다.";
            }
        }

        Long paymentTypeId = attractionDto.getPaymentTypeId();
        if(paymentTypeId == null) {
            return "결제방법이 입력되지 않았습니다.";
        }

        return null;
    }
}
