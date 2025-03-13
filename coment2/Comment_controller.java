package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.Comment_dto;
import org.spring.attraction.service.Comment_service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class Comment_controller {

    private final Comment_service commentService;


    @PostMapping("/write")// 댓글 작성-로그인된 사용자만 가능
    public ResponseEntity<Comment_dto> writeComment(@AuthenticationPrincipal User user,@RequestParam Long boardId,
            @RequestParam String content) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment_dto commentDTO = commentService.writeComment(user.getId(), boardId, content);
        return ResponseEntity.ok(commentDTO);
    }

 
    @PutMapping("/update/{commentId}")   // 댓글 수정-작성자만 가능
    public ResponseEntity<Comment_dto> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user,
            @RequestParam String newContent) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment_dto commentDTO = commentService.updateComment(commentId, user.getId(), newContent);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

   
    @DeleteMapping("/delete/{commentId}")//댓글삭제-작성자와 관리자만 가능
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean isDeleted = commentService.deleteComment(commentId, user);
        return isDeleted ? ResponseEntity.ok("Comment deleted successfully") 
                         : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to delete comment");
    }

    
    @GetMapping("/board/{boardId}")// 특정게시글의 모든댓글을조회
    public ResponseEntity<List<Comment_dto>> getCommentsByBoard(@PathVariable Long boardId) {
        List<Comment_dto> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(comments);
    }
}
 