package org.spring.attraction.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.Comment_dto;
import org.spring.attraction.entity.Board;
import org.spring.attraction.entity.Comment;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.Board_repository;
import org.spring.attraction.repository.Comment_repository;
import org.spring.attraction.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Comment_service {

    private final Comment_repository commentRepository;
    private final Board_repository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment_dto writeComment(User user, Long boardId, String content) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

        Comment comment = new Comment();
        comment.setUser(user);  // 전달받은 User 엔티티 사용
        comment.setBoard(board);
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());

        return Comment_dto.toDTO(commentRepository.save(comment));
    }

    @Transactional
    public Comment_dto updateComment(Long commentId, Long userId, String newContent) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment != null && comment.getUser().getId().equals(userId)) {
            comment.setContent(newContent);
            comment.setUpdateDate(LocalDateTime.now());
            return Comment_dto.toDTO(commentRepository.save(comment));
        }

        return null;
    }

    @Transactional
    public boolean deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment != null) {
            boolean isOwner = comment.getUser().getId().equals(user.getId());
            boolean isAdmin = user.getUserType().equals("manager");

            if (isOwner || isAdmin) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public List<Comment_dto> getCommentsByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream().map(Comment_dto::toDTO).collect(Collectors.toList());
    }
}

