package com.korine.koboard.controller.dto

import com.korine.koboard.service.dto.PostSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
    val tags: String? = null,
)

fun Page<PostSummaryResponseDto>.toResponse() = PageImpl(
    content.map { it.toResponse() },
    pageable,
    totalElements
)

fun PostSummaryResponseDto.toResponse() = PostSummaryResponse(
    id = id,
    title = title,
    createdBy = createdBy,
    createdAt = createdAt,
)
