package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.AttractionType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttractionTypeDto {
    private Long id;
    private String type;

    public static AttractionTypeDto toDto(AttractionType attractionType) {
        AttractionTypeDto attractionTypeDto = new AttractionTypeDto();
        attractionTypeDto.setId(attractionType.getId());
        attractionTypeDto.setType(attractionType.getType());
        return attractionTypeDto;
    }

    public static String validate(AttractionTypeDto attractionTypeDto) {
        String attractionTypeType = attractionTypeDto.getType().trim();
        if(attractionTypeType.isEmpty()) {
            return "구분이 입력되지 않았습니다.";
        }else if(attractionTypeType.length() > 10) {
            return "구분은 1~10자로 입력이 가능합니다.";
        }
        return null;
    }
}
