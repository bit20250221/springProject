package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.ENUM.AreaMessage;
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

    public static AreaMessage validate(AreaDto areaDto) {
        String areaCountry = areaDto.getCountry().trim();
        if(areaCountry.isEmpty()) {
            return AreaMessage.getTypeById(-3);
        }else if(areaCountry.length() > 10) {
            return AreaMessage.getTypeById(-4);
        }
        String areaCity = areaDto.getCity().trim();
        if(areaCity.isEmpty()) {
            return AreaMessage.getTypeById(-5);
        }else if(areaCity.length() > 10) {
            return AreaMessage.getTypeById(-6);
        }

        return null;
    }
}