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

}
