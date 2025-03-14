package org.spring.attraction.repository;

import org.spring.attraction.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardImage_repository extends JpaRepository<BoardImage,Long> {
    BoardImage findBoardImageByUUID(String uuid);
    BoardImage findBoardImageByUUIDAndUUIDName(String uuid, String uuidName);
    @Query(value = "select * from db_springproject.boardimage where uuid = ?1 and uuidname = ?2 and board_id = ?3"
            , nativeQuery = true)
    BoardImage findBoardImageByUUIDAndUUIDNameAndBoardId(String uuid,String uuidName,Long boardId);
    List<BoardImage> findAllByBoard_Id(Long boardId);
    void deleteByBoard_Id(Long boardId);
    void deleteByUUID(String uuid);
    void deleteByUUIDAndUUIDName(String uuid, String uuidName);
}
