package org.spring.attraction.repository;

import org.spring.attraction.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Comment_repository extends JpaRepository<Comment, Long> {
}
