package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.BoardImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage_dto {
    private Long id;
    private String name;
    private String UUID;
    private String UUIDName;
    private int ImageSize;
    private String ImagePath;
    private Long boardId;
    private Boolean isTemporary;
    private MultipartFile img;

    public static BoardImage_dto boardImageDto(BoardImage boardImage){
        return new BoardImage_dto(
                boardImage.getId(),
                boardImage.getName(),
                boardImage.getUUID(),
                boardImage.getUUIDName(),
                boardImage.getImageSize(),
                boardImage.getImagePath(),
                boardImage.getBoard()!=null? boardImage.getBoard().getId() : null,
                boardImage.getIsTemporary(),
                null
        );
    }
    public static BoardImage_dto boardImageDto2(MultipartFile multipartFile)  {
        BoardImage_dto dto=new BoardImage_dto();
        dto.setName(multipartFile.getOriginalFilename());
        dto.setImageSize((int)multipartFile.getSize());
        dto.setImg(multipartFile);

        return dto;
    }
}
