package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.Area;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AreaDto {
    private Long id;
    private String country;
    private String city;

    public static AreaDto toDto(Area area) {
        AreaDto areaDto = new AreaDto();
        areaDto.setId(area.getId());
        areaDto.setCountry(area.getCountry());
        areaDto.setCity(area.getCity());
        return areaDto;
    }

    public static String validate(AreaDto areaDto) {
        String areaCountry = areaDto.getCountry().trim();
        if(areaCountry.isEmpty()) {
            return "국가가 입력되지 않았습니다.";
        }else if(areaCountry.length() > 10) {
            return "국가는 1~10자로 입력이 가능합니다.";
        }
        String areaCity = areaDto.getCity().trim();
        if(areaCity.isEmpty()) {
            return "도시가 입력되지 않았습니다.";
        }else if(areaCity.length() > 10) {
            return "도시는 1~10자로 입력이 가능합니다.";
        }

        return null;
    }
}
