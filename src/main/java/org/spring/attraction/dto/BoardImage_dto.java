package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.BoardImage;
import org.springframework.web.multipart.MultipartFile;

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

    public static BoardImage_dto boardImageDto(BoardImage boardImage){
        return new BoardImage_dto(
                boardImage.getId(),
                boardImage.getName(),
                boardImage.getUUID(),
                boardImage.getUUIDName(),
                boardImage.getImageSize(),
                boardImage.getImagePath(),
                boardImage.getBoard()!=null? boardImage.getBoard().getId() : null,
                boardImage.getIsTemporary()
        );
    }
    public static BoardImage_dto boardImageDto2(MultipartFile multipartFile){
        return new BoardImage_dto(
                null,
                multipartFile.getOriginalFilename(),
                null,
                null,
                (int)multipartFile.getSize(),
                null,
                null,
                true
        );
    }
}
