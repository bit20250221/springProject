package org.spring.attraction.repository;

import org.spring.attraction.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Comment_repository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.board.id = :boardId")
    void deleteByBoardId(Long boardId);
}

