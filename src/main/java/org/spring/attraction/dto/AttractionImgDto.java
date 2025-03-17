package org.spring.attraction.dto;

import lombok.*;
import org.spring.attraction.entity.AttractionImg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttractionImgDto {

    @Value("${app.img-dir}")
    public String IMG_DIR_URL;
    //public static final String IMG_DIR_URL = "C:/boot-study/springProject2/src/main/resources/static/img";
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
