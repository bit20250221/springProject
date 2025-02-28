package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment_dto {
    private Long id;
    private String content;
    private Long user_id;
    private Long board_id;
    private LocalDateTime createdate;
    private LocalDateTime updatedate;

    public static Comment_dto to_dto(Comment entity, Long user_id, Long board_id){
        Comment_dto dto=new Comment_dto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setUser_id(user_id);
        dto.setBoard_id(board_id);
        dto.setCreatedate(entity.getCreateDate());
        dto.setUpdatedate(entity.getUpdateDate());
        return dto;
    }
}
