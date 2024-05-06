package com.korine.koboard.controller

import com.korine.koboard.controller.dto.CommentCreateRequest
import com.korine.koboard.controller.dto.CommentUpdateRequest
import com.korine.koboard.controller.dto.toDto
import com.korine.koboard.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest
    ): Long {
        return commentService.createComment(postId, commentCreateRequest.toDto())
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest
    ): Long {
        return 0L
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String
    ): Long {
        return commentService.deleteComment(commentId, deletedBy)
    }
}
