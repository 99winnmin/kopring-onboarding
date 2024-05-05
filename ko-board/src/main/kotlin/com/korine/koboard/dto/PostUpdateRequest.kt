package com.korine.koboard.dto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)
