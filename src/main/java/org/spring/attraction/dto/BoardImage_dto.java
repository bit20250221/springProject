package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.BoardImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage_dto {
    private Long id;
    private String name;
    private int ImageSize;
    private String ImagePath;
    private Long boardId;
    private Boolean isTemporary;

    public static BoardImage_dto boardImageDto(BoardImage boardImage){

        return new BoardImage_dto(
                boardImage.getId(),
                boardImage.getName(),
                boardImage.getImageSize(),
                boardImage.getImagePath(),
                boardImage.getBoard().getId(),
                boardImage.getIsTemporary()
        );
    }
}
