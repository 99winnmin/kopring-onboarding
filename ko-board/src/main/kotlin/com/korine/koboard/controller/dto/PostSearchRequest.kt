package com.korine.koboard.controller.dto

import com.korine.koboard.service.dto.PostSearchRequestDto
import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val createdBy: String?,
    @RequestParam
    val tags: String?,
)

fun PostSearchRequest.toDto() = PostSearchRequestDto(
    title = title,
    createdBy = createdBy,
)
