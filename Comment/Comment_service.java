package org.spring.attraction.service;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.Comment_dto;
import org.spring.attraction.entity.Board;
import org.spring.attraction.entity.Comment;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.BoardRepository;
import org.spring.attraction.repository.Comment_repository;
import org.spring.attraction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Comment_service {

    private final Comment_repository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment_dto writeComment(Long userId, Long boardId, String content) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (userOptional.isPresent() && boardOptional.isPresent()) {
            User user = userOptional.get();
            Board board = boardOptional.get();

            Comment comment = new Comment();
            comment.setUser(user);
            comment.setBoard(board);
            comment.setContent(content);
            comment.setCreateDate(LocalDateTime.now());

            Comment savedComment = commentRepository.save(comment);

            return Comment_dto.toDTO(savedComment);
        } else {
            return null;
        }
    }

    @Transactional
    public Comment_dto updateComment(Long commentId, Long userId, String newContent) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            if (comment.getUser().getId().equals(userId)) {
                comment.setContent(newContent);
                comment.setUpdateDate(LocalDateTime.now());

                Comment updatedComment = commentRepository.save(comment);

                return Comment_dto.toDTO(updatedComment);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();

            if (comment.getUser().getId().equals(userId)) {
                commentRepository.delete(comment);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Transactional
    public Comment_dto getComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            return Comment_dto.toDTO(commentOptional.get());
        } else {
            return null;
        }
    }
}
