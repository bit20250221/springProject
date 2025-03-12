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

    @PostMapping("/write") // 댓글 작성
    public ResponseEntity<Comment_dto> writeComment(@AuthenticationPrincipal Long userId, @RequestParam Long boardId, @RequestParam String content) {
        Comment_dto commentDTO = commentService.writeComment(userId, boardId, content);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().body(null);
    }

    @PutMapping("/update/{commentId}") // 댓글 수정
    public ResponseEntity<Comment_dto> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal Long userId, @RequestParam String newContent) {
        Comment_dto commentDTO = commentService.updateComment(commentId, userId, newContent);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.badRequest().body(null);
    }

    @DeleteMapping("/delete/{commentId}") // 댓글 삭제
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal Long userId) {
        boolean isDeleted = commentService.deleteComment(commentId, userId);
        return isDeleted ? ResponseEntity.ok("Comment deleted successfully") : ResponseEntity.badRequest().body("Failed to delete comment");
    }

    @GetMapping("/get/{commentId}") // 댓글 조회
    public ResponseEntity<Comment_dto> getComment(@PathVariable Long commentId) {
        Comment_dto commentDTO = commentService.getComment(commentId);
        return commentDTO != null ? ResponseEntity.ok(commentDTO) : ResponseEntity.notFound().build();
    }
}
