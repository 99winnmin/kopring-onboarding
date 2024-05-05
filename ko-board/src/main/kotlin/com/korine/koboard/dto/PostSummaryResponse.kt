package com.korine.koboard.dto

import java.time.LocalDateTime

data class PostSummaryResponse (
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
)
