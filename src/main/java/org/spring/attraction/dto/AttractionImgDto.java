package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.AttractionImg;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttractionImgDto {
    public static final String IMG_DIR_URL = "C:/Users/seung/IdeaProjects/springProject/src/main/resources/static/img";

    private Long id;

    private String name;

    private String UUID;

    private Long attractionId;

    private MultipartFile img;

    public static AttractionImgDto toDto(AttractionImg attractionImg) {
        AttractionImgDto attractionImgDto = new AttractionImgDto();
        attractionImgDto.setId(attractionImg.getId());
        attractionImgDto.setName(attractionImg.getName());
        attractionImgDto.setUUID(attractionImg.getUUID());
        return attractionImgDto;
    }
}
