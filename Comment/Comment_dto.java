package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.entity.Comment;

@Getter
@Setter
@AllArgsConstructor
public class Comment_dto {

    private Long id;
    private Long userId;
    private Long boardId;
    private String content;
    private String createDate;
    private String updateDate;

    public static Comment_dto toDTO(Comment commentEntity) {
        return new Comment_dto(
                commentEntity.getId(),
                commentEntity.getUser().getId(),
                commentEntity.getBoard().getId(),
                commentEntity.getContent(),
                commentEntity.getCreateDate().toString(),
                commentEntity.getUpdateDate() != null ? commentEntity.getUpdateDate().toString() : null
        );
    }
}
