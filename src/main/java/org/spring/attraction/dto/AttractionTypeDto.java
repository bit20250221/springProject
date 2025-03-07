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
}