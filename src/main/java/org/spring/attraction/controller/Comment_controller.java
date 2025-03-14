package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.dto.Comment_dto;
import org.spring.attraction.dto.user.CustomUserDetails;
import org.spring.attraction.entity.User;
import org.spring.attraction.service.Board_service;
import org.spring.attraction.service.Comment_service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class Comment_controller {

    private final Comment_service commentService;
    private final Board_service boardService;
    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()") // 댓글 작성-로그인된 사용자만 가능
    public ResponseEntity<?> writeComment(
            @AuthenticationPrincipal CustomUserDetails customUser,
            @RequestParam Long boardId,
            @RequestParam String content) {
        Map<String, Object> ResponseMap = new HashMap<>();
        Comment_dto commentDTO = commentService.writeComment(customUser.getUser(), boardId, content);

        if(commentDTO!=null) {
            ResponseMap.put("comment",commentDTO);
            return ResponseEntity.ok(ResponseMap);
        }else{
            ResponseMap.put("message", "Failure");
            return ResponseEntity.ok(ResponseMap);
        }
    }

    @PutMapping("/update/{commentId}")
    @PreAuthorize("isAuthenticated()") // 댓글 수정-작성자만 가능
    public ResponseEntity<Comment_dto> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUser,
            @RequestParam String newContent) {

        Comment_dto commentDTO = commentService.updateComment(commentId, customUser.getUser().getId(), newContent);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete/{commentId}")// 댓글 삭제-작성자와 관리자만 가능 
    @PreAuthorize("hasRole('ROLE_ADMIN') or isAuthenticated()") 
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails customUser) {

        boolean isDeleted = commentService.deleteComment(commentId, customUser.getUser());
        return isDeleted ? ResponseEntity.ok("Comment deleted successfully")
                         : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to delete comment");
    }

    @GetMapping("/board/{boardId}")
    @PreAuthorize("permitAll()") // 특정 게시글의모든댓글 조회
    public ResponseEntity<List<Comment_dto>> getCommentsByBoard(@AuthenticationPrincipal CustomUserDetails customUser,@PathVariable Long boardId) {
        Board_dto board=boardService.getBoard(boardId);
        //다른 글 댓글 못읽어오게 하는 관련 로직 추가(리뷰, 일반은 모두 댓글 읽을 수 있게, 문의, 신고, 리뷰는 댓글을 일부만 읽을 수 있게 수정)

        List<Comment_dto> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(comments);
    }
}

 