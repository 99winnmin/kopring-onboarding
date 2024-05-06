package com.korine.koboard.domain

import com.korine.koboard.exception.CommentNotUpdatableException
import com.korine.koboard.service.dto.CommentUpdateRequestDto
import jakarta.persistence.*

@Entity
class Comment(
    content: String,
    post: Post,
    createdBy: String,
): BaseEntity(createdBy = createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var post: Post = post
        protected set

    fun update(updateRequestDto: CommentUpdateRequestDto) {
        if (updateRequestDto.updatedBy != createdBy) {
            throw CommentNotUpdatableException()
        }
        content = updateRequestDto.content
        super.updatedBy(updateRequestDto.updatedBy)
    }
}
