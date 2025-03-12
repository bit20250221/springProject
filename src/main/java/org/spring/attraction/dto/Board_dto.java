package org.spring.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.entity.Board;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

    //평소에는 안 사용하다가 사용자들에게 게시판에 보여줄때 필요
    private String user_login_Id;
    private String attraction_Name;
    private int comment_Num;
    private List<MultipartFile> imglist;
    private List<BoardImage_dto> boardImages;

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

    public static Board_dto to_dto_2(Board entity, Long user_id, Long attraction_id){
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

        dto.setUser_login_Id(entity.getUser().getUserLoginId());
        if(entity.getAttraction()!=null) {
            dto.setAttraction_Name(entity.getAttraction().getName());
        }else{
            dto.setAttraction_Name("NONE");
        }
        dto.setComment_Num(entity.getCommentCount());
        return dto;
    }
}
