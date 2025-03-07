package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.ENUM.PayType;
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

    public static String validate(AttractionDto attractionDto) {
        String attractionName = attractionDto.getName().trim();
        if(attractionName.isEmpty()) {
            return "이름이 입력되지 않았습니다.";
        }else if(attractionName.length() > 10) {
            return "이름은 1~10자로 입력이 가능합니다.";
        }

        int attractionPrice = attractionDto.getPrice();
        if(attractionPrice < 0) {
            return "가격은 음수를 입력할 수 없습니다.";
        }else if(attractionPrice > 500000 || attractionPrice < 1000) {
            return "가격은 1000~500000범위 내에서 입력이 가능합니다.";
        }

        List<Long> attractionTypeDtoIdList = attractionDto.getAttractionTypeDtoIdList();
        if(attractionTypeDtoIdList.isEmpty()) {
            return "관광지 구분은 최소 1개 이상 선택하셔야 합니다.";
        }else if(attractionTypeDtoIdList.size() > 3) {
            return "관광지 구분은 최대 3개까지 선택이 가능합니다.";
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
