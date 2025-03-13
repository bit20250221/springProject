package org.spring.attraction.repository;

import org.spring.attraction.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Comment_repository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);
}

