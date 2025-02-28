package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.Board;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board_dto {
    private Long board_id;
    private String tab;
    private String title;
    private String content;
    private Long user_id;
    private Long attraction_id;
    private Integer rate;
    private LocalDateTime creartedate;
    private LocalDateTime updatedate;

    public static Board_dto to_dto(Board entity, Long user_id, Long attraction_id){
        Board_dto dto=new Board_dto();
        dto.setBoard_id(entity.getId());
        dto.setTab(entity.getTab().name());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setRate(entity.getRate());
        dto.setCreartedate(entity.getCreateDate());
        dto.setUpdatedate(entity.getUpdateDate());

        dto.setUser_id(user_id);
        dto.setAttraction_id(attraction_id);

        return dto;
    }
}
