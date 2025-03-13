package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.AttractionTypeList;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttractionTypeListDto {
    private Long id;
    private Long attractionId;
    private Long attractionTypeId;


    public static AttractionTypeListDto toEntity(AttractionTypeList attractionTypeList) {
        AttractionTypeListDto attractionTypeListDto = new AttractionTypeListDto();
        attractionTypeListDto.setId(attractionTypeList.getId());
        return attractionTypeListDto;
    }
}
