package com.korine.koboard.domain

import com.korine.koboard.exception.PostNotUpdatableException
import com.korine.koboard.service.dto.PostUpdateRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Post(
    createdBy: String,
    title: String,
    content: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var title: String = title
        protected set
    var content: String = content
        protected set

    fun update(postupdateRequestDto: PostUpdateRequestDto) {
        if (postupdateRequestDto.updatedBy != createdBy) {
            throw PostNotUpdatableException()
        }
        title = postupdateRequestDto.title
        content = postupdateRequestDto.content
        super.updatedBy(postupdateRequestDto.updatedBy)
    }
}
