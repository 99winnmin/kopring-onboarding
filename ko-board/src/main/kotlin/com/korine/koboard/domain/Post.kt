package com.korine.koboard.domain

import com.korine.koboard.exception.PostNotUpdatableException
import com.korine.koboard.service.dto.PostUpdateRequestDto
import jakarta.persistence.*

@Entity
class Post(
    createdBy: String,
    title: String,
    content: String,
    tags: List<String> = emptyList()
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var title: String = title
        protected set
    var content: String = content
        protected set

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    var tags: MutableList<Tag> = tags.map { Tag(it, this, createdBy) }.toMutableList()
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
