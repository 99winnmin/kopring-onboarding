package com.korine.koboard.service.dto

import com.korine.koboard.domain.Comment
import com.korine.koboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
