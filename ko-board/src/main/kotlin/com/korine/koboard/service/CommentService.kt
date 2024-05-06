package com.korine.koboard.service

import com.korine.koboard.exception.CommentNotDeletableException
import com.korine.koboard.exception.CommentNotFoundException
import com.korine.koboard.exception.PostNotFoundException
import com.korine.koboard.repository.CommentRepository
import com.korine.koboard.repository.PostRepository
import com.korine.koboard.service.dto.CommentCreateRequestDto
import com.korine.koboard.service.dto.CommentUpdateRequestDto
import com.korine.koboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    @Transactional
    fun createComment(postId: Long, createRequestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(createRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(commentId: Long, updateRequestDto: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        comment.update(updateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(commentId: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        if(comment.createdBy != deletedBy) {
            throw CommentNotDeletableException()
        }
        commentRepository.delete(comment)
        return commentId
    }
}
