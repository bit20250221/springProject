package org.spring.attraction.repository;

import org.spring.attraction.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImage_repository extends JpaRepository<BoardImage,Long> {
    BoardImage findBoardImageByUUID(String uuid);
    BoardImage findBoardImageByUUIDAndUUIDName(String uuid, String uuidName);
    BoardImage findBoardImageByUUIDAndUUIDNameAndBoard_Id(String uuid,String uuidName, Long boardId);
    List<BoardImage> findAllByBoard_Id(Long boardId);
    void deleteByBoard_Id(Long boardId);
    void deleteByUUID(String uuid);
    void deleteByUUIDAndUUIDName(String uuid, String uuidName);
}
