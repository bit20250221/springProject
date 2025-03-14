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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        String boardTab=board.getTab().name();
        String Auth=user.getUserType().name();
        if(Auth.compareTo("nomal")==0 &&
                !((boardTab.compareTo("신고")==0 || boardTab.compareTo("문의")==0)&&
                        board.getUser().getUserLoginId().compareTo(user.getUserLoginId())==0)&&
                (boardTab.compareTo("공지")==0)){
            //댓글 입력 불가
            return null;
        }
        if(Auth.compareTo("attraction")==0 &&
                !((boardTab.compareTo("신고")==0 || boardTab.compareTo("문의")==0)&&
                        ((board.getUser().getUserLoginId().compareTo(user.getUserLoginId())==0)||
                                Objects.equals(board.getAttraction().getId(), user.getAttraction().getId())))&&
                (boardTab.compareTo("공지")==0)){
            //댓글 입력 불가
            return null;
        }
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
            int isAdmin = user.getUserType().name().compareTo("manager");

            if (isOwner || isAdmin==0) {
                commentRepository.delete(comment);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public ArrayList<Comment_dto> getCommentsByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return (ArrayList<Comment_dto>) comments.stream().map(Comment_dto::toDTO).collect(Collectors.toList());
    }
}

